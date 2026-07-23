package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Medicine
import com.example.data.model.Patient
import com.example.data.model.PrescriptionItem
import com.example.ui.theme.SiddhaGoldAccent
import com.example.ui.viewmodel.SiddhaViewModel

@Composable
fun PrescriptionBuilderScreen(
    viewModel: SiddhaViewModel,
    modifier: Modifier = Modifier
) {
    val selectedPatient by viewModel.draftSelectedPatient.collectAsState()
    val nadiDiagnostics by viewModel.draftNadiDiagnostics.collectAsState()
    val chiefComplaints by viewModel.draftChiefComplaints.collectAsState()
    val consultationFee by viewModel.draftConsultationFee.collectAsState()
    val discount by viewModel.draftDiscount.collectAsState()
    val paymentMode by viewModel.draftPaymentMode.collectAsState()
    val items by viewModel.draftPrescriptionItems.collectAsState()

    var isAddMedicinePickerOpen by remember { mutableStateOf(false) }
    var isSelectPatientPickerOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Section Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ReceiptLong,
                contentDescription = "Rx Builder",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "New Prescription & Bill",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "Step-by-step easy billing for Siddha clinic",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ----------------------------------------------------
        // STEP 1: PATIENT SELECTION
        // ----------------------------------------------------
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Step 1: Patient Information",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (selectedPatient == null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { isSelectPatientPickerOpen = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_select_patient")
                        ) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "Select Patient")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Select Patient", fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.showAddPatientDialog() },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_add_new_patient")
                        ) {
                            Icon(imageVector = Icons.Default.PersonAdd, contentDescription = "New Patient")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "+ Register New", fontSize = 13.sp)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = selectedPatient!!.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = "${selectedPatient!!.age}Y / ${selectedPatient!!.gender} • Ph: ${selectedPatient!!.phone}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            TextButton(
                                onClick = { isSelectPatientPickerOpen = true },
                                modifier = Modifier.testTag("btn_change_patient")
                            ) {
                                Text(text = "Change", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Nadi / Diagnostic notes
                OutlinedTextField(
                    value = nadiDiagnostics,
                    onValueChange = { viewModel.draftNadiDiagnostics.value = it },
                    label = { Text("Nadi Evaluation / Siddha Diagnosis (Optional)") },
                    placeholder = { Text("e.g., Pitham Elevated, Body Heat, Joint Pain") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_nadi_diagnosis"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = chiefComplaints,
                    onValueChange = { viewModel.draftChiefComplaints.value = it },
                    label = { Text("Chief Complaints") },
                    placeholder = { Text("e.g. Gastric trouble, lower back ache for 2 weeks") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_chief_complaints"),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ----------------------------------------------------
        // STEP 2: PRESCRIBED MEDICINES
        // ----------------------------------------------------
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Step 2: Medicines Prescribed (${items.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Button(
                        onClick = { isAddMedicinePickerOpen = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("btn_add_medicine_to_rx")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Medicine", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "+ Add Medicine", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (items.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No medicines added yet. Tap '+ Add Medicine' to select from inventory.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items.forEachIndexed { idx, item ->
                        PrescriptionItemRow(
                            item = item,
                            onRemove = { viewModel.removeMedicineFromPrescription(item) }
                        )
                        if (idx < items.size - 1) {
                            Divider(modifier = Modifier.padding(vertical = 6.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ----------------------------------------------------
        // STEP 3: BILLING & PAYMENT SUMMARY
        // ----------------------------------------------------
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Step 3: Billing & Total Calculation",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = consultationFee.toString(),
                        onValueChange = {
                            val fee = it.toDoubleOrNull() ?: 0.0
                            viewModel.draftConsultationFee.value = fee
                        },
                        label = { Text("Consultation Fee (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_consultation_fee"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    OutlinedTextField(
                        value = discount.toString(),
                        onValueChange = {
                            val disc = it.toDoubleOrNull() ?: 0.0
                            viewModel.draftDiscount.value = disc
                        },
                        label = { Text("Discount (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_discount"),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Payment mode picker
                Text(text = "Payment Mode:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Cash", "UPI", "Card", "Pending").forEach { mode ->
                        val isSel = paymentMode == mode
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { viewModel.draftPaymentMode.value = mode }
                                .padding(vertical = 8.dp)
                                .testTag("payment_mode_$mode"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = mode,
                                color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Total Calculation Box
                val medicinesTotal = items.sumOf { it.totalPrice }
                val grandTotal = (consultationFee + medicinesTotal - discount).coerceAtLeast(0.0)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Medicines Total:", fontSize = 13.sp)
                        Text(text = "₹${String.format("%.2f", medicinesTotal)}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Consultation Fee:", fontSize = 13.sp)
                        Text(text = "₹${String.format("%.2f", consultationFee)}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    if (discount > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Discount:", fontSize = 13.sp, color = Color(0xFFDC2626))
                            Text(text = "-₹${String.format("%.2f", discount)}", fontSize = 13.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "GRAND TOTAL:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "₹${String.format("%.2f", grandTotal)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Generate & Print Button
                Button(
                    onClick = {
                        viewModel.generateBillAndSavePrescription { savedPrescription ->
                            viewModel.openPrescriptionDetail(savedPrescription)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("btn_generate_bill"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = "Generate")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "GENERATE BILL & PRESCRIPTION",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // ----------------------------------------------------
    // SELECT PATIENT DIALOG
    // ----------------------------------------------------
    if (isSelectPatientPickerOpen) {
        SelectPatientDialog(
            viewModel = viewModel,
            onDismiss = { isSelectPatientPickerOpen = false },
            onSelect = { patient ->
                viewModel.selectPatientForPrescription(patient)
                isSelectPatientPickerOpen = false
            }
        )
    }

    // ----------------------------------------------------
    // ADD MEDICINE PICKER DIALOG
    // ----------------------------------------------------
    if (isAddMedicinePickerOpen) {
        AddMedicineToRxDialog(
            viewModel = viewModel,
            onDismiss = { isAddMedicinePickerOpen = false },
            onAdd = { medicine, dosage, timing, anupanam, duration, qty ->
                viewModel.addMedicineToPrescription(
                    medicine = medicine,
                    dosage = dosage,
                    timing = timing,
                    anupanam = anupanam,
                    durationDays = duration,
                    quantity = qty
                )
                isAddMedicinePickerOpen = false
            }
        )
    }
}

@Composable
fun PrescriptionItemRow(
    item: PrescriptionItem,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.medicineName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "${item.dosage} (${item.timing}) • Vehicle: ${item.anupanam}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${item.durationDays} Days • Qty: ${item.quantity} • Total: ₹${String.format("%.2f", item.totalPrice)}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        IconButton(onClick = onRemove, modifier = Modifier.testTag("remove_rx_item_${item.medicineId}")) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove",
                tint = Color.Red.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun SelectPatientDialog(
    viewModel: SiddhaViewModel,
    onDismiss: () -> Unit,
    onSelect: (Patient) -> Unit
) {
    val patients by viewModel.patients.collectAsState()
    var search by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Select Patient", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = search,
                    onValueChange = {
                        search = it
                        viewModel.patientSearchQuery.value = it
                    },
                    placeholder = { Text("Search by name or phone...") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    items(patients) { patient ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(patient) }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = patient.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(
                                    text = "${patient.age}Y/${patient.gender} • Ph: ${patient.phone}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Button(
                                onClick = { onSelect(patient) },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(text = "Select", fontSize = 12.sp)
                            }
                        }
                        Divider()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineToRxDialog(
    viewModel: SiddhaViewModel,
    onDismiss: () -> Unit,
    onAdd: (Medicine, String, String, String, Int, Int) -> Unit
) {
    val medicines by viewModel.medicines.collectAsState()
    var search by remember { mutableStateOf("") }
    var selectedMedicine by remember { mutableStateOf<Medicine?>(null) }

    var dosage by remember { mutableStateOf("1-0-1") }
    var timing by remember { mutableStateOf("After Food") }
    var anupanam by remember { mutableStateOf("Warm Water") }
    var durationDays by remember { mutableStateOf("7") }
    var quantity by remember { mutableStateOf("1") }

    val anupanamList = listOf("Warm Water", "Honey", "Milk", "Hot Water", "Ghee", "Ginger Juice")
    val timingList = listOf("After Food", "Before Food", "At Bedtime", "Before Bath")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Add Siddha Medicine to Rx", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (selectedMedicine == null) {
                    OutlinedTextField(
                        value = search,
                        onValueChange = {
                            search = it
                            viewModel.medicineSearchQuery.value = it
                        },
                        placeholder = { Text("Search medicine from inventory...") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(modifier = Modifier.height(280.dp)) {
                        items(medicines) { medicine ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedMedicine = medicine
                                        dosage = medicine.defaultDosage
                                        timing = medicine.timing
                                        anupanam = medicine.defaultAnupanam
                                    }
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = medicine.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(
                                        text = "${medicine.category} • Stock: ${medicine.stockQuantity}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "₹${String.format("%.2f", medicine.pricePerUnit)}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Divider()
                        }
                    }
                } else {
                    val med = selectedMedicine!!
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = med.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(text = "${med.category} • ₹${med.pricePerUnit} per ${med.unit}", fontSize = 12.sp)
                            }
                            TextButton(onClick = { selectedMedicine = null }) {
                                Text(text = "Change", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = dosage,
                        onValueChange = { dosage = it },
                        label = { Text("Dosage") },
                        placeholder = { Text("e.g., 1-0-1 or 5g twice daily") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Anupanam Dropdown
                    var expandedAnupanam by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedAnupanam,
                        onExpandedChange = { expandedAnupanam = !expandedAnupanam }
                    ) {
                        OutlinedTextField(
                            value = anupanam,
                            onValueChange = { anupanam = it },
                            label = { Text("Anupanam (Vehicle)") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAnupanam) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expandedAnupanam,
                            onDismissRequest = { expandedAnupanam = false }
                        ) {
                            anupanamList.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(text = option) },
                                    onClick = {
                                        anupanam = option
                                        expandedAnupanam = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Timing Dropdown
                    var expandedTiming by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedTiming,
                        onExpandedChange = { expandedTiming = !expandedTiming }
                    ) {
                        OutlinedTextField(
                            value = timing,
                            onValueChange = { timing = it },
                            label = { Text("Timing") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTiming) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTiming,
                            onDismissRequest = { expandedTiming = false }
                        ) {
                            timingList.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(text = option) },
                                    onClick = {
                                        timing = option
                                        expandedTiming = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = durationDays,
                            onValueChange = { durationDays = it },
                            label = { Text("Duration (Days)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text("Quantity (${med.unit})") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val dur = durationDays.toIntOrNull() ?: 1
                    val qty = quantity.toIntOrNull() ?: 1
                    val totPrice = med.pricePerUnit * qty

                    Text(
                        text = "Calculated Cost: ₹${String.format("%.2f", totPrice)}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onAdd(med, dosage, timing, anupanam, dur, qty)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Confirm & Add to Bill", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
