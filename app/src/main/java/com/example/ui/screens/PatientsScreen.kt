package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Patient
import com.example.ui.components.PatientCard
import com.example.ui.viewmodel.SiddhaViewModel

@Composable
fun PatientsScreen(
    viewModel: SiddhaViewModel,
    modifier: Modifier = Modifier
) {
    val patients by viewModel.patients.collectAsState()
    val searchQuery by viewModel.patientSearchQuery.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Header & Search
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "Patients Directory",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Patient Records",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "${patients.size} patients registered",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Button(
                onClick = { viewModel.showAddPatientDialog() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("btn_register_patient")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Patient", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Add Patient", fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.patientSearchQuery.value = it },
            placeholder = { Text("Search by patient name or phone number...") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_search_patient"),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (patients.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (searchQuery.isNotBlank()) "No patients matching '$searchQuery'" else "No patient records stored yet. Tap 'Add Patient' to create one.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(patients, key = { it.id }) { patient ->
                    PatientCard(
                        patient = patient,
                        onSelectForRx = {
                            viewModel.selectPatientForPrescription(patient)
                            viewModel.selectTab(0) // Switch to Prescription Builder tab
                        },
                        onViewHistory = {
                            viewModel.openPatientDetail(patient)
                        },
                        onEdit = {
                            viewModel.showEditPatientDialog(patient)
                        },
                        onDelete = {
                            viewModel.deletePatient(patient)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PatientFormDialog(
    editingPatient: Patient?,
    onDismiss: () -> Unit,
    onSave: (Patient) -> Unit
) {
    var name by remember { mutableStateOf(editingPatient?.name ?: "") }
    var age by remember { mutableStateOf(editingPatient?.age?.toString() ?: "") }
    var gender by remember { mutableStateOf(editingPatient?.gender ?: "Male") }
    var phone by remember { mutableStateOf(editingPatient?.phone ?: "") }
    var address by remember { mutableStateOf(editingPatient?.address ?: "") }
    var nadiDiagnostics by remember { mutableStateOf(editingPatient?.nadiDiagnostics ?: "") }
    var chiefComplaints by remember { mutableStateOf(editingPatient?.chiefComplaints ?: "") }
    var medicalHistory by remember { mutableStateOf(editingPatient?.medicalHistory ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (editingPatient == null) "Register New Patient" else "Edit Patient Record",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Patient Full Name*") },
                    modifier = Modifier.fillMaxWidth().testTag("form_patient_name"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Age*") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("form_patient_age"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number*") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.weight(1.5f).testTag("form_patient_phone"),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Gender selector
                Text(text = "Gender:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Male", "Female", "Other").forEach { g ->
                        val isSel = gender == g
                        Button(
                            onClick = { gender = g },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = g,
                                color = if (isSel) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nadiDiagnostics,
                    onValueChange = { nadiDiagnostics = it },
                    label = { Text("Nadi / Tridosha Evaluation") },
                    placeholder = { Text("e.g. Vatham: Normal, Pitham: High") },
                    modifier = Modifier.fillMaxWidth().testTag("form_patient_nadi"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = chiefComplaints,
                    onValueChange = { chiefComplaints = it },
                    label = { Text("Chief Complaints") },
                    modifier = Modifier.fillMaxWidth().testTag("form_patient_complaints"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address / City") },
                    modifier = Modifier.fillMaxWidth().testTag("form_patient_address"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val ageInt = age.toIntOrNull() ?: 0
                        if (name.isNotBlank() && phone.isNotBlank()) {
                            onSave(
                                Patient(
                                    id = editingPatient?.id ?: 0,
                                    name = name,
                                    age = ageInt,
                                    gender = gender,
                                    phone = phone,
                                    address = address,
                                    nadiDiagnostics = nadiDiagnostics,
                                    chiefComplaints = chiefComplaints,
                                    medicalHistory = medicalHistory
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("form_save_patient"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Save Patient Record", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
