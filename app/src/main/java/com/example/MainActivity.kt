package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BillPrintDialog
import com.example.ui.components.BottomNavBar
import com.example.ui.components.TopHeader
import com.example.ui.screens.ClinicSettingsDialog
import com.example.ui.screens.InventoryScreen
import com.example.ui.screens.MedicineFormDialog
import com.example.ui.screens.PastBillsScreen
import com.example.ui.screens.PatientDetailDialog
import com.example.ui.screens.PatientFormDialog
import com.example.ui.screens.PatientsScreen
import com.example.ui.screens.PrescriptionBuilderScreen
import com.example.ui.theme.SiddhaClinicTheme
import com.example.ui.viewmodel.SiddhaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SiddhaClinicTheme {
                SiddhaClinicApp()
            }
        }
    }
}

@Composable
fun SiddhaClinicApp(
    viewModel: SiddhaViewModel = viewModel()
) {
    val context = LocalContext.current
    val selectedTab by viewModel.selectedTab.collectAsState()
    val clinicSettings by viewModel.clinicSettings.collectAsState()
    val toastMessage by viewModel.userToastMessage.collectAsState()

    // Dialog states
    val viewingPrescription by viewModel.viewingPrescription.collectAsState()
    val viewingPrescriptionItems by viewModel.viewingPrescriptionItems.collectAsState()

    val viewingPatient by viewModel.viewingPatient.collectAsState()
    val viewingPatientPrescriptions by viewModel.viewingPatientPrescriptions.collectAsState()

    val isMedicineDialogVisible by viewModel.isMedicineDialogVisible.collectAsState()
    val editingMedicine by viewModel.editingMedicine.collectAsState()

    val isPatientDialogVisible by viewModel.isPatientDialogVisible.collectAsState()
    val editingPatient by viewModel.editingPatient.collectAsState()

    val isSettingsDialogVisible by viewModel.isSettingsDialogVisible.collectAsState()

    // Show toast feedback
    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopHeader(
                clinicSettings = clinicSettings,
                onOpenSettings = { viewModel.showSettingsDialog() }
            )
        },
        bottomBar = {
            BottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { tab ->
                when (tab) {
                    0 -> PrescriptionBuilderScreen(viewModel = viewModel)
                    1 -> PatientsScreen(viewModel = viewModel)
                    2 -> InventoryScreen(viewModel = viewModel)
                    3 -> PastBillsScreen(viewModel = viewModel)
                }
            }
        }

        // ----------------------------------------------------
        // GLOBAL DIALOGS
        // ----------------------------------------------------

        // 1. Bill & Prescription Detail/Print Dialog
        if (viewingPrescription != null) {
            BillPrintDialog(
                prescription = viewingPrescription!!,
                items = viewingPrescriptionItems,
                clinicSettings = clinicSettings,
                onDismiss = { viewModel.closePrescriptionDetail() }
            )
        }

        // 2. Patient Record & History Dialog
        if (viewingPatient != null) {
            PatientDetailDialog(
                patient = viewingPatient!!,
                historyPrescriptions = viewingPatientPrescriptions,
                onDismiss = { viewModel.closePatientDetail() },
                onOpenPrescription = { prescription ->
                    viewModel.openPrescriptionDetail(prescription)
                },
                onCreateNewBill = {
                    viewModel.selectPatientForPrescription(viewingPatient!!)
                    viewModel.closePatientDetail()
                    viewModel.selectTab(0)
                }
            )
        }

        // 3. Add / Edit Medicine Dialog
        if (isMedicineDialogVisible) {
            MedicineFormDialog(
                editingMedicine = editingMedicine,
                onDismiss = { viewModel.hideMedicineDialog() },
                onSave = { medicine -> viewModel.saveMedicine(medicine) }
            )
        }

        // 4. Add / Edit Patient Dialog
        if (isPatientDialogVisible) {
            PatientFormDialog(
                editingPatient = editingPatient,
                onDismiss = { viewModel.hidePatientDialog() },
                onSave = { patient -> viewModel.savePatient(patient) }
            )
        }

        // 5. Clinic Settings Dialog
        if (isSettingsDialogVisible) {
            ClinicSettingsDialog(
                currentSettings = clinicSettings,
                onDismiss = { viewModel.hideSettingsDialog() },
                onSave = { settings -> viewModel.saveClinicSettings(settings) }
            )
        }
    }
}
