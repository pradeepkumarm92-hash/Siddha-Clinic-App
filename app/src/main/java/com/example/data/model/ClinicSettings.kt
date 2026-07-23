package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clinic_settings")
data class ClinicSettings(
    @PrimaryKey
    val id: Int = 1,
    val clinicName: String = "Sri Siddha Herbal Healthcare Clinic",
    val tagline: String = "Traditional Siddha Healing & Wellness Center",
    val vaidyarName: String = "Dr. S. Vaidyanathan B.S.M.S.",
    val registrationNumber: String = "REG-BSMS-48921",
    val phone: String = "+91 98765 43210",
    val address: String = "No. 42, Temple Road, Herbal Nagar, Tamil Nadu",
    val defaultConsultationFee: Double = 200.0,
    val gstTaxPercentage: Double = 0.0,
    val customFooterMessage: String = "Pathiya Unavu (Dietary Discipline) is essential for Siddha medicine efficacy. Get well soon!"
)
