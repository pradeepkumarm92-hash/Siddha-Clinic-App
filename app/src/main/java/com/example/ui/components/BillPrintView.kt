package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.ClinicSettings
import com.example.data.model.Prescription
import com.example.data.model.PrescriptionItem
import com.example.ui.theme.SiddhaGoldAccent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BillPrintDialog(
    prescription: Prescription,
    items: List<PrescriptionItem>,
    clinicSettings: ClinicSettings?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .testTag("bill_print_dialog"),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Official Prescription & Bill",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_bill_dialog")) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Scrollable Bill Paper
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Banner Header Image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .clip(RoundedCornerShape(6.dp))
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.siddha_clinic_banner_1784822505126),
                                contentDescription = "Clinic Banner",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .background(Color.Black.copy(alpha = 0.45f))
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = prescription.clinicName,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = clinicSettings?.tagline ?: "Traditional Siddha Healthcare & Wellness",
                                    color = SiddhaGoldAccent,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Doctor & Clinic Info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = prescription.vaidyarName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF1B4D3E)
                                )
                                Text(
                                    text = "Reg No: ${clinicSettings?.registrationNumber ?: "REG-BSMS-48921"}",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "Ph: ${clinicSettings?.phone ?: "+91 98765 43210"}",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Bill No: ${prescription.billNumber}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                                val dateStr = SimpleDateFormat(
                                    "dd MMM yyyy, hh:mm a",
                                    Locale.getDefault()
                                ).format(Date(prescription.prescribedDate))
                                Text(
                                    text = "Date: $dateStr",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = Color.LightGray)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Patient Info Table
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF9FAFB))
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(6.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Patient: ${prescription.patientName}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "Age/Gender: ${prescription.patientAge} Yrs / ${prescription.patientGender}",
                                        fontSize = 12.sp,
                                        color = Color.DarkGray
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Phone: ${prescription.patientPhone}",
                                        fontSize = 12.sp,
                                        color = Color.DarkGray
                                    )
                                }
                                if (prescription.nadiSummary.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Nadi Evaluation: ${prescription.nadiSummary}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF166534)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Rx Symbol
                        Text(
                            text = "Rx (Prescribed Siddha Formulations)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFF1B4D3E)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Medicines Table
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(6.dp))
                        ) {
                            // Table Header
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF1B4D3E))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Medicine",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(2.2f)
                                )
                                Text(
                                    text = "Dosage & Vehicle",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(2f)
                                )
                                Text(
                                    text = "Days",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(0.8f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Price",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.End
                                )
                            }

                            // Items
                            items.forEachIndexed { index, item ->
                                val rowBg = if (index % 2 == 0) Color.White else Color(0xFFF9FAFB)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(rowBg)
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(2.2f)) {
                                        Text(
                                            text = item.medicineName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = item.category,
                                            fontSize = 10.sp,
                                            color = Color.Gray
                                        )
                                    }

                                    Column(modifier = Modifier.weight(2f)) {
                                        Text(
                                            text = "${item.dosage} (${item.timing})",
                                            fontSize = 11.sp,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = "Anupanam: ${item.anupanam}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFFB8860B)
                                        )
                                    }

                                    Text(
                                        text = "${item.durationDays}d",
                                        fontSize = 11.sp,
                                        modifier = Modifier.weight(0.8f),
                                        textAlign = TextAlign.Center,
                                        color = Color.Black
                                    )

                                    Text(
                                        text = "₹${String.format("%.2f", item.totalPrice)}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.End,
                                        color = Color.Black
                                    )
                                }
                                if (index < items.size - 1) {
                                    Divider(color = Color(0xFFF3F4F6))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Billing Calculation Breakdown
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF9FAFB))
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(6.dp))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Consultation Fee:",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "₹${String.format("%.2f", prescription.consultationFee)}",
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Medicines Total:",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "₹${String.format("%.2f", prescription.medicinesTotal)}",
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                            }

                            if (prescription.discountAmount > 0) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Discount:",
                                        fontSize = 12.sp,
                                        color = Color(0xFFDC2626)
                                    )
                                    Text(
                                        text = "-₹${String.format("%.2f", prescription.discountAmount)}",
                                        fontSize = 12.sp,
                                        color = Color(0xFFDC2626)
                                    )
                                }
                            }

                            Divider(modifier = Modifier.padding(vertical = 6.dp), color = Color.Gray)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "GRAND TOTAL:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color(0xFF1B4D3E)
                                    )
                                    Text(
                                        text = "Payment: ${prescription.paymentMode} (PAID)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF166534)
                                    )
                                }
                                Text(
                                    text = "₹${String.format("%.2f", prescription.grandTotal)}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF1B4D3E)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Pathiya Unavu / Dietary Discipline Footer
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFFBEB))
                                .border(1.dp, Color(0xFFFCD34D), RoundedCornerShape(6.dp))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "📌 Pathiya Unavu Note: ${clinicSettings?.customFooterMessage ?: "Follow strict dietary discipline as advised by Vaidyar for optimal healing."}",
                                fontSize = 11.sp,
                                color = Color(0xFF92400E),
                                lineHeight = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Signature
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Dr. S. Vaidyanathan B.S.M.S.",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Serif,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Chief Siddha Physician / Vaidyar",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dialog Buttons (Share / Print)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            sharePrescriptionText(context, prescription, items, clinicSettings)
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("share_bill_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Share Bill", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            sharePrescriptionText(context, prescription, items, clinicSettings)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("print_bill_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Print, contentDescription = "Print", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Print / Export", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

private fun sharePrescriptionText(
    context: Context,
    prescription: Prescription,
    items: List<PrescriptionItem>,
    settings: ClinicSettings?
) {
    val dateStr = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Date(prescription.prescribedDate))
    val sb = StringBuilder()
    sb.appendLine("🌿 ${prescription.clinicName}")
    sb.appendLine("${settings?.tagline ?: "Siddha Healthcare Center"}")
    sb.appendLine("Vaidyar: ${prescription.vaidyarName}")
    sb.appendLine("----------------------------------")
    sb.appendLine("Bill No: ${prescription.billNumber} | Date: $dateStr")
    sb.appendLine("Patient: ${prescription.patientName} (${prescription.patientAge}Y/${prescription.patientGender})")
    sb.appendLine("Phone: ${prescription.patientPhone}")
    if (prescription.nadiSummary.isNotBlank()) {
        sb.appendLine("Nadi Evaluation: ${prescription.nadiSummary}")
    }
    sb.appendLine("----------------------------------")
    sb.appendLine("Rx Prescribed Siddha Medicines:")
    items.forEachIndexed { i, item ->
        sb.appendLine("${i + 1}. ${item.medicineName} (${item.category})")
        sb.appendLine("   Dosage: ${item.dosage} | Timing: ${item.timing}")
        sb.appendLine("   Anupanam (Vehicle): ${item.anupanam}")
        sb.appendLine("   Duration: ${item.durationDays} Days | ₹${String.format("%.2f", item.totalPrice)}")
    }
    sb.appendLine("----------------------------------")
    sb.appendLine("Consultation Fee: ₹${String.format("%.2f", prescription.consultationFee)}")
    sb.appendLine("Medicines Total: ₹${String.format("%.2f", prescription.medicinesTotal)}")
    if (prescription.discountAmount > 0) {
        sb.appendLine("Discount: -₹${String.format("%.2f", prescription.discountAmount)}")
    }
    sb.appendLine("GRAND TOTAL: ₹${String.format("%.2f", prescription.grandTotal)} (${prescription.paymentMode} - PAID)")
    sb.appendLine("----------------------------------")
    sb.appendLine("📌 Note: ${settings?.customFooterMessage ?: "Follow strict Pathiya Unavu dietary discipline."}")

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, sb.toString())
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share Prescription & Bill")
    context.startActivity(shareIntent)
}
