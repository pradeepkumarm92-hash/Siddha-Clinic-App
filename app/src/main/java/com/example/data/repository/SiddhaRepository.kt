package com.example.data.repository

import com.example.data.SeedData
import com.example.data.local.ClinicSettingsDao
import com.example.data.local.MedicineDao
import com.example.data.local.PatientDao
import com.example.data.local.PrescriptionDao
import com.example.data.model.ClinicSettings
import com.example.data.model.Medicine
import com.example.data.model.Patient
import com.example.data.model.Prescription
import com.example.data.model.PrescriptionItem
import kotlinx.coroutines.flow.Flow

class SiddhaRepository(
    private val medicineDao: MedicineDao,
    private val patientDao: PatientDao,
    private val prescriptionDao: PrescriptionDao,
    private val clinicSettingsDao: ClinicSettingsDao
) {
    // Flow getters
    val allMedicines: Flow<List<Medicine>> = medicineDao.getAllMedicines()
    val allPatients: Flow<List<Patient>> = patientDao.getAllPatients()
    val allPrescriptions: Flow<List<Prescription>> = prescriptionDao.getAllPrescriptions()
    val clinicSettings: Flow<ClinicSettings?> = clinicSettingsDao.getClinicSettings()
    val totalRevenue: Flow<Double?> = prescriptionDao.getTotalRevenue()

    suspend fun checkAndSeedDatabase() {
        if (medicineDao.getCount() == 0) {
            medicineDao.insertAll(SeedData.initialMedicines)
        }
        if (patientDao.getCount() == 0) {
            patientDao.insertAll(SeedData.initialPatients)
        }
        if (clinicSettingsDao.getClinicSettingsSync() == null) {
            clinicSettingsDao.saveSettings(SeedData.defaultSettings)
        }
    }

    // Medicine Operations
    fun searchMedicines(query: String): Flow<List<Medicine>> = medicineDao.searchMedicines(query)
    suspend fun insertMedicine(medicine: Medicine): Long = medicineDao.insertMedicine(medicine)
    suspend fun updateMedicine(medicine: Medicine) = medicineDao.updateMedicine(medicine)
    suspend fun deleteMedicine(medicine: Medicine) = medicineDao.deleteMedicine(medicine)

    // Patient Operations
    fun searchPatients(query: String): Flow<List<Patient>> = patientDao.searchPatients(query)
    suspend fun insertPatient(patient: Patient): Long = patientDao.insertPatient(patient)
    suspend fun updatePatient(patient: Patient) = patientDao.updatePatient(patient)
    suspend fun deletePatient(patient: Patient) = patientDao.deletePatient(patient)
    suspend fun getPatientById(id: Int): Patient? = patientDao.getPatientById(id)

    // Prescription & Billing Operations
    fun getPrescriptionsByPatient(patientId: Int): Flow<List<Prescription>> =
        prescriptionDao.getPrescriptionsByPatientId(patientId)

    fun getPrescriptionItems(prescriptionId: Int): Flow<List<PrescriptionItem>> =
        prescriptionDao.getItemsForPrescription(prescriptionId)

    suspend fun getPrescriptionItemsSync(prescriptionId: Int): List<PrescriptionItem> =
        prescriptionDao.getItemsForPrescriptionSync(prescriptionId)

    suspend fun savePrescription(
        prescription: Prescription,
        items: List<PrescriptionItem>
    ): Long {
        val id = prescriptionDao.insertPrescription(prescription)
        val preparedItems = items.map { it.copy(prescriptionId = id.toInt()) }
        prescriptionDao.insertPrescriptionItems(preparedItems)

        // Deduct stock for each medicine
        preparedItems.forEach { item ->
            medicineDao.deductStock(item.medicineId, item.quantity)
        }

        return id
    }

    suspend fun deletePrescription(prescriptionId: Int) {
        prescriptionDao.deletePrescriptionWithItems(prescriptionId)
    }

    // Settings
    suspend fun updateClinicSettings(settings: ClinicSettings) {
        clinicSettingsDao.saveSettings(settings)
    }

    suspend fun getClinicSettingsSync(): ClinicSettings {
        return clinicSettingsDao.getClinicSettingsSync() ?: SeedData.defaultSettings
    }
}
