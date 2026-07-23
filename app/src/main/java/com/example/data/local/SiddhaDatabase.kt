package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ClinicSettings
import com.example.data.model.Medicine
import com.example.data.model.Patient
import com.example.data.model.Prescription
import com.example.data.model.PrescriptionItem

@Database(
    entities = [
        Medicine::class,
        Patient::class,
        Prescription::class,
        PrescriptionItem::class,
        ClinicSettings::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SiddhaDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun patientDao(): PatientDao
    abstract fun prescriptionDao(): PrescriptionDao
    abstract fun clinicSettingsDao(): ClinicSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: SiddhaDatabase? = null

        fun getDatabase(context: Context): SiddhaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SiddhaDatabase::class.java,
                    "siddha_clinic_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
