package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val category: String, // Choornam, Kudineer, Thailam, Leghyam, Parpam, Cheendhooram, Tablet, Syrup
    val stockQuantity: Int,
    val unit: String, // Grams, ml, Tablets, Packs, Bottles
    val pricePerUnit: Double,
    val defaultDosage: String = "1-0-1",
    val defaultAnupanam: String = "Warm Water", // Honey, Warm Water, Milk, Hot Water, Ghee, etc.
    val timing: String = "After Food", // Before Food, After Food
    val description: String = ""
)
