package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class Patient(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val age: Int,
    val gender: String, // Male, Female, Other
    val phone: String,
    val address: String = "",
    val nadiDiagnostics: String = "", // Vatham, Pitham, Kapham evaluation
    val chiefComplaints: String = "",
    val medicalHistory: String = "",
    val createdDate: Long = System.currentTimeMillis()
)
