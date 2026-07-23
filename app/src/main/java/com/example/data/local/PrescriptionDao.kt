package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.model.Prescription
import com.example.data.model.PrescriptionItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PrescriptionDao {
    @Query("SELECT * FROM prescriptions ORDER BY prescribedDate DESC")
    fun getAllPrescriptions(): Flow<List<Prescription>>

    @Query("SELECT * FROM prescriptions WHERE id = :id")
    suspend fun getPrescriptionById(id: Int): Prescription?

    @Query("SELECT * FROM prescriptions WHERE patientId = :patientId ORDER BY prescribedDate DESC")
    fun getPrescriptionsByPatientId(patientId: Int): Flow<List<Prescription>>

    @Query("SELECT * FROM prescription_items WHERE prescriptionId = :prescriptionId")
    fun getItemsForPrescription(prescriptionId: Int): Flow<List<PrescriptionItem>>

    @Query("SELECT * FROM prescription_items WHERE prescriptionId = :prescriptionId")
    suspend fun getItemsForPrescriptionSync(prescriptionId: Int): List<PrescriptionItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrescription(prescription: Prescription): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrescriptionItems(items: List<PrescriptionItem>)

    @Query("SELECT COUNT(*) FROM prescriptions")
    suspend fun getCount(): Int

    @Query("SELECT SUM(grandTotal) FROM prescriptions")
    fun getTotalRevenue(): Flow<Double?>

    @Query("DELETE FROM prescriptions WHERE id = :id")
    suspend fun deletePrescriptionById(id: Int)

    @Query("DELETE FROM prescription_items WHERE prescriptionId = :prescriptionId")
    suspend fun deletePrescriptionItems(prescriptionId: Int)

    @Transaction
    suspend fun deletePrescriptionWithItems(prescriptionId: Int) {
        deletePrescriptionItems(prescriptionId)
        deletePrescriptionById(prescriptionId)
    }
}
