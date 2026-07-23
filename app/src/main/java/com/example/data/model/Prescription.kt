package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prescriptions")
data class Prescription(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val billNumber: String,
    val patientId: Int,
    val patientName: String,
    val patientAge: Int,
    val patientGender: String,
    val patientPhone: String,
    val vaidyarName: String,
    val clinicName: String,
    val nadiSummary: String = "",
    val chiefComplaints: String = "",
    val consultationFee: Double = 0.0,
    val medicinesTotal: Double = 0.0,
    val discountAmount: Double = 0.0,
    val grandTotal: Double = 0.0,
    val paymentMode: String = "Cash", // Cash, UPI, Card, Pending
    val isPaid: Boolean = true,
    val prescribedDate: Long = System.currentTimeMillis(),
    val notes: String = ""
)
