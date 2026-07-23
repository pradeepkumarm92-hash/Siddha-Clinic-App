package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SiddhaDatabase
import com.example.data.model.ClinicSettings
import com.example.data.model.Medicine
import com.example.data.model.Patient
import com.example.data.model.Prescription
import com.example.data.model.PrescriptionItem
import com.example.data.repository.SiddhaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SiddhaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SiddhaRepository

    init {
        val db = SiddhaDatabase.getDatabase(application)
        repository = SiddhaRepository(
            db.medicineDao(),
            db.patientDao(),
            db.prescriptionDao(),
            db.clinicSettingsDao()
        )
        viewModelScope.launch {
            repository.checkAndSeedDatabase()
        }
    }

    // Navigation state
    private val _selectedTab = MutableStateFlow(0) // 0: Prescription Builder, 1: Patients, 2: Inventory, 3: Past Bills
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun selectTab(tab: Int) {
        _selectedTab.value = tab
    }

    // Search queries
    val medicineSearchQuery = MutableStateFlow("")
    val patientSearchQuery = MutableStateFlow("")
    val billSearchQuery = MutableStateFlow("")

    // Medicines List
    @OptIn(ExperimentalCoroutinesApi::class)
    val medicines: StateFlow<List<Medicine>> = medicineSearchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) repository.allMedicines else repository.searchMedicines(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Patients List
    @OptIn(ExperimentalCoroutinesApi::class)
    val patients: StateFlow<List<Patient>> = patientSearchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) repository.allPatients else repository.searchPatients(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Prescriptions List
    @OptIn(ExperimentalCoroutinesApi::class)
    val prescriptions: StateFlow<List<Prescription>> = combine(
        repository.allPrescriptions,
        billSearchQuery
    ) { list, query ->
        if (query.isBlank()) {
            list
        } else {
            list.filter {
                it.patientName.contains(query, ignoreCase = true) ||
                        it.billNumber.contains(query, ignoreCase = true) ||
                        it.patientPhone.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Clinic Settings
    val clinicSettings: StateFlow<ClinicSettings?> = repository.clinicSettings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Total Revenue
    val totalRevenue: StateFlow<Double?> = repository.totalRevenue
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // ----------------------------------------------------
    // PRESCRIPTION DRAFT & BILL GENERATOR STATE
    // ----------------------------------------------------
    val draftSelectedPatient = MutableStateFlow<Patient?>(null)
    val draftNadiDiagnostics = MutableStateFlow("")
    val draftChiefComplaints = MutableStateFlow("")
    val draftConsultationFee = MutableStateFlow(200.0)
    val draftDiscount = MutableStateFlow(0.0)
    val draftPaymentMode = MutableStateFlow("Cash")
    val draftPrescriptionItems = MutableStateFlow<List<PrescriptionItem>>(emptyList())
    val userToastMessage = MutableStateFlow<String?>(null)

    fun selectPatientForPrescription(patient: Patient) {
        draftSelectedPatient.value = patient
        draftNadiDiagnostics.value = patient.nadiDiagnostics
        draftChiefComplaints.value = patient.chiefComplaints
    }

    fun addMedicineToPrescription(
        medicine: Medicine,
        dosage: String,
        timing: String,
        anupanam: String,
        durationDays: Int,
        quantity: Int
    ) {
        val unitPrice = medicine.pricePerUnit
        val totalPrice = unitPrice * quantity

        val newItem = PrescriptionItem(
            prescriptionId = 0,
            medicineId = medicine.id,
            medicineName = medicine.name,
            category = medicine.category,
            dosage = dosage,
            timing = timing,
            anupanam = anupanam,
            durationDays = durationDays,
            quantity = quantity,
            unitPrice = unitPrice,
            totalPrice = totalPrice
        )

        val currentList = draftPrescriptionItems.value.toMutableList()
        // Replace if already exists, else append
        val existingIndex = currentList.indexOfFirst { it.medicineId == medicine.id }
        if (existingIndex >= 0) {
            currentList[existingIndex] = newItem
        } else {
            currentList.add(newItem)
        }
        draftPrescriptionItems.value = currentList
        userToastMessage.value = "${medicine.name} added to prescription"
    }

    fun removeMedicineFromPrescription(item: PrescriptionItem) {
        val currentList = draftPrescriptionItems.value.toMutableList()
        currentList.remove(item)
        draftPrescriptionItems.value = currentList
    }

    fun clearPrescriptionDraft() {
        draftSelectedPatient.value = null
        draftNadiDiagnostics.value = ""
        draftChiefComplaints.value = ""
        draftConsultationFee.value = 200.0
        draftDiscount.value = 0.0
        draftPaymentMode.value = "Cash"
        draftPrescriptionItems.value = emptyList()
    }

    fun generateBillAndSavePrescription(onSuccess: (Prescription) -> Unit) {
        val patient = draftSelectedPatient.value
        if (patient == null) {
            userToastMessage.value = "Please select or add a patient first!"
            return
        }

        if (draftPrescriptionItems.value.isEmpty() && draftConsultationFee.value <= 0) {
            userToastMessage.value = "Please add medicines or consultation fee!"
            return
        }

        viewModelScope.launch {
            val settings = repository.getClinicSettingsSync()
            val medicinesTotal = draftPrescriptionItems.value.sumOf { it.totalPrice }
            val consultation = draftConsultationFee.value
            val discount = draftDiscount.value
            val grandTotal = (consultation + medicinesTotal - discount).coerceAtLeast(0.0)

            val dateString = SimpleDateFormat("yyyyMMdd-HHmm", Locale.getDefault()).format(Date())
            val billNo = "SCB-$dateString"

            val prescription = Prescription(
                billNumber = billNo,
                patientId = patient.id,
                patientName = patient.name,
                patientAge = patient.age,
                patientGender = patient.gender,
                patientPhone = patient.phone,
                vaidyarName = settings.vaidyarName,
                clinicName = settings.clinicName,
                nadiSummary = draftNadiDiagnostics.value,
                chiefComplaints = draftChiefComplaints.value,
                consultationFee = consultation,
                medicinesTotal = medicinesTotal,
                discountAmount = discount,
                grandTotal = grandTotal,
                paymentMode = draftPaymentMode.value,
                isPaid = true,
                prescribedDate = System.currentTimeMillis()
            )

            val newId = repository.savePrescription(prescription, draftPrescriptionItems.value)
            val savedPrescription = prescription.copy(id = newId.toInt())

            clearPrescriptionDraft()
            userToastMessage.value = "Bill & Prescription $billNo saved successfully!"
            onSuccess(savedPrescription)
        }
    }

    // ----------------------------------------------------
    // DIALOG & SELECTION STATES
    // ----------------------------------------------------
    val viewingPrescription = MutableStateFlow<Prescription?>(null)
    val viewingPrescriptionItems = MutableStateFlow<List<PrescriptionItem>>(emptyList())
    val viewingPatient = MutableStateFlow<Patient?>(null)
    val viewingPatientPrescriptions = MutableStateFlow<List<Prescription>>(emptyList())

    val editingMedicine = MutableStateFlow<Medicine?>(null)
    val isMedicineDialogVisible = MutableStateFlow(false)

    val editingPatient = MutableStateFlow<Patient?>(null)
    val isPatientDialogVisible = MutableStateFlow(false)

    val isSettingsDialogVisible = MutableStateFlow(false)

    fun openPrescriptionDetail(prescription: Prescription) {
        viewingPrescription.value = prescription
        viewModelScope.launch {
            viewingPrescriptionItems.value = repository.getPrescriptionItemsSync(prescription.id)
        }
    }

    fun closePrescriptionDetail() {
        viewingPrescription.value = null
        viewingPrescriptionItems.value = emptyList()
    }

    fun openPatientDetail(patient: Patient) {
        viewingPatient.value = patient
        viewModelScope.launch {
            repository.getPrescriptionsByPatient(patient.id).collect {
                viewingPatientPrescriptions.value = it
            }
        }
    }

    fun closePatientDetail() {
        viewingPatient.value = null
        viewingPatientPrescriptions.value = emptyList()
    }

    // Medicine CRUD
    fun showAddMedicineDialog() {
        editingMedicine.value = null
        isMedicineDialogVisible.value = true
    }

    fun showEditMedicineDialog(medicine: Medicine) {
        editingMedicine.value = medicine
        isMedicineDialogVisible.value = true
    }

    fun hideMedicineDialog() {
        isMedicineDialogVisible.value = false
        editingMedicine.value = null
    }

    fun saveMedicine(medicine: Medicine) {
        viewModelScope.launch {
            if (medicine.id == 0) {
                repository.insertMedicine(medicine)
                userToastMessage.value = "${medicine.name} added to inventory"
            } else {
                repository.updateMedicine(medicine)
                userToastMessage.value = "${medicine.name} updated"
            }
            hideMedicineDialog()
        }
    }

    fun deleteMedicine(medicine: Medicine) {
        viewModelScope.launch {
            repository.deleteMedicine(medicine)
            userToastMessage.value = "${medicine.name} removed from inventory"
        }
    }

    // Patient CRUD
    fun showAddPatientDialog() {
        editingPatient.value = null
        isPatientDialogVisible.value = true
    }

    fun showEditPatientDialog(patient: Patient) {
        editingPatient.value = patient
        isPatientDialogVisible.value = true
    }

    fun hidePatientDialog() {
        isPatientDialogVisible.value = false
        editingPatient.value = null
    }

    fun savePatient(patient: Patient, selectForPrescriptionAfterSave: Boolean = false) {
        viewModelScope.launch {
            if (patient.id == 0) {
                val id = repository.insertPatient(patient)
                val newPatient = patient.copy(id = id.toInt())
                userToastMessage.value = "Patient ${patient.name} registered"
                if (selectForPrescriptionAfterSave) {
                    selectPatientForPrescription(newPatient)
                }
            } else {
                repository.updatePatient(patient)
                userToastMessage.value = "Patient ${patient.name} details updated"
            }
            hidePatientDialog()
        }
    }

    fun deletePatient(patient: Patient) {
        viewModelScope.launch {
            repository.deletePatient(patient)
            userToastMessage.value = "Patient ${patient.name} deleted"
            if (viewingPatient.value?.id == patient.id) {
                closePatientDetail()
            }
        }
    }

    fun deletePrescription(prescription: Prescription) {
        viewModelScope.launch {
            repository.deletePrescription(prescription.id)
            userToastMessage.value = "Prescription ${prescription.billNumber} deleted"
            if (viewingPrescription.value?.id == prescription.id) {
                closePrescriptionDetail()
            }
        }
    }

    // Settings
    fun showSettingsDialog() {
        isSettingsDialogVisible.value = true
    }

    fun hideSettingsDialog() {
        isSettingsDialogVisible.value = false
    }

    fun saveClinicSettings(settings: ClinicSettings) {
        viewModelScope.launch {
            repository.updateClinicSettings(settings)
            userToastMessage.value = "Clinic settings updated"
            hideSettingsDialog()
        }
    }

    fun clearToast() {
        userToastMessage.value = null
    }
}
