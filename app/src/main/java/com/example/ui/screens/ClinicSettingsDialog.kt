package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.ClinicSettings

@Composable
fun ClinicSettingsDialog(
    currentSettings: ClinicSettings?,
    onDismiss: () -> Unit,
    onSave: (ClinicSettings) -> Unit
) {
    var clinicName by remember { mutableStateOf(currentSettings?.clinicName ?: "Sri Siddha Clinic") }
    var tagline by remember { mutableStateOf(currentSettings?.tagline ?: "Traditional Healing Center") }
    var vaidyarName by remember { mutableStateOf(currentSettings?.vaidyarName ?: "Dr. S. Vaidyanathan B.S.M.S.") }
    var registrationNumber by remember { mutableStateOf(currentSettings?.registrationNumber ?: "REG-BSMS-48921") }
    var phone by remember { mutableStateOf(currentSettings?.phone ?: "+91 98765 43210") }
    var address by remember { mutableStateOf(currentSettings?.address ?: "No. 42, Temple Road, Herbal Nagar") }
    var defaultConsultationFee by remember { mutableStateOf(currentSettings?.defaultConsultationFee?.toString() ?: "200.0") }
    var customFooterMessage by remember { mutableStateOf(currentSettings?.customFooterMessage ?: "Follow strict Pathiya Unavu dietary discipline.") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
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
                    Text(
                        text = "Clinic & Vaidyar Profile",
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
                    value = clinicName,
                    onValueChange = { clinicName = it },
                    label = { Text("Clinic Name*") },
                    modifier = Modifier.fillMaxWidth().testTag("settings_clinic_name"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = tagline,
                    onValueChange = { tagline = it },
                    label = { Text("Clinic Tagline") },
                    modifier = Modifier.fillMaxWidth().testTag("settings_tagline"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = vaidyarName,
                    onValueChange = { vaidyarName = it },
                    label = { Text("Vaidyar / Physician Name*") },
                    modifier = Modifier.fillMaxWidth().testTag("settings_vaidyar_name"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = registrationNumber,
                    onValueChange = { registrationNumber = it },
                    label = { Text("BSMS Reg. Number") },
                    modifier = Modifier.fillMaxWidth().testTag("settings_reg_no"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Clinic Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth().testTag("settings_phone"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Clinic Address") },
                    modifier = Modifier.fillMaxWidth().testTag("settings_address"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = defaultConsultationFee,
                    onValueChange = { defaultConsultationFee = it },
                    label = { Text("Default Consultation Fee (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("settings_default_fee"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = customFooterMessage,
                    onValueChange = { customFooterMessage = it },
                    label = { Text("Pathiya Unavu / Footer Note on Bills") },
                    modifier = Modifier.fillMaxWidth().testTag("settings_footer_note"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val fee = defaultConsultationFee.toDoubleOrNull() ?: 200.0
                        onSave(
                            ClinicSettings(
                                id = 1,
                                clinicName = clinicName,
                                tagline = tagline,
                                vaidyarName = vaidyarName,
                                registrationNumber = registrationNumber,
                                phone = phone,
                                address = address,
                                defaultConsultationFee = fee,
                                customFooterMessage = customFooterMessage
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("settings_save_btn"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Save Settings", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
