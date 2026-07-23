package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prescription_items")
data class PrescriptionItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val prescriptionId: Int,
    val medicineId: Int,
    val medicineName: String,
    val category: String,
    val dosage: String, // e.g., "1-0-1", "5g twice daily"
    val timing: String, // "Before Food", "After Food"
    val anupanam: String, // "Warm Water", "Honey", "Milk", "Hot Water"
    val durationDays: Int,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double
)
