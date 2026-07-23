package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Medicine
import com.example.ui.components.MedicineCard
import com.example.ui.viewmodel.SiddhaViewModel

@Composable
fun InventoryScreen(
    viewModel: SiddhaViewModel,
    modifier: Modifier = Modifier
) {
    val medicines by viewModel.medicines.collectAsState()
    val searchQuery by viewModel.medicineSearchQuery.collectAsState()

    var selectedCategoryFilter by remember { mutableStateOf("All") }
    val categories = listOf("All", "Choornam", "Kudineer / Kashayam", "Thailam", "Leghyam", "Parpam & Cheendhooram", "Tablet / Vati", "Syrup / Manappagu")

    val filteredMedicines = remember(medicines, selectedCategoryFilter) {
        if (selectedCategoryFilter == "All") {
            medicines
        } else {
            medicines.filter { it.category.contains(selectedCategoryFilter, ignoreCase = true) }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Top Title & Add Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Inventory2,
                    contentDescription = "Inventory",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Medicine Inventory",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "${medicines.size} Siddha formulations stocked",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Button(
                onClick = { viewModel.showAddMedicineDialog() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("btn_add_medicine")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Medicine", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Add Stock", fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.medicineSearchQuery.value = it },
            placeholder = { Text("Search medicine name, category...") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_search_medicine"),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Category Filter Horizontal Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSel = selectedCategoryFilter == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { selectedCategoryFilter = cat }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("filter_category_$cat")
                ) {
                    Text(
                        text = cat,
                        color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredMedicines.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (searchQuery.isNotBlank()) "No medicine found matching '$searchQuery'" else "No stock in this category.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredMedicines, key = { it.id }) { medicine ->
                    MedicineCard(
                        medicine = medicine,
                        onEdit = { viewModel.showEditMedicineDialog(medicine) },
                        onDelete = { viewModel.deleteMedicine(medicine) },
                        onAddToPrescription = {
                            viewModel.showEditMedicineDialog(medicine)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineFormDialog(
    editingMedicine: Medicine?,
    onDismiss: () -> Unit,
    onSave: (Medicine) -> Unit
) {
    var name by remember { mutableStateOf(editingMedicine?.name ?: "") }
    var category by remember { mutableStateOf(editingMedicine?.category ?: "Choornam") }
    var stockQuantity by remember { mutableStateOf(editingMedicine?.stockQuantity?.toString() ?: "50") }
    var unit by remember { mutableStateOf(editingMedicine?.unit ?: "Packs") }
    var pricePerUnit by remember { mutableStateOf(editingMedicine?.pricePerUnit?.toString() ?: "50.0") }
    var defaultDosage by remember { mutableStateOf(editingMedicine?.defaultDosage ?: "1-0-1") }
    var defaultAnupanam by remember { mutableStateOf(editingMedicine?.defaultAnupanam ?: "Warm Water") }
    var timing by remember { mutableStateOf(editingMedicine?.timing ?: "After Food") }
    var description by remember { mutableStateOf(editingMedicine?.description ?: "") }

    val categoryList = listOf("Choornam", "Kudineer / Kashayam", "Thailam", "Leghyam", "Parpam & Cheendhooram", "Tablet / Vati", "Syrup / Manappagu")
    val unitList = listOf("Grams", "Packs", "Bottles", "Jars", "Containers", "Strip (10 Tab)", "ml")
    val anupanamList = listOf("Warm Water", "Honey", "Milk", "Hot Water", "Ghee", "Ginger Juice")

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
                        text = if (editingMedicine == null) "Add Siddha Medicine" else "Edit Medicine Stock",
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
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Medicine Name* (e.g. Amukkara Choornam)") },
                    modifier = Modifier.fillMaxWidth().testTag("form_med_name"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Category Dropdown
                var expandedCat by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedCat,
                    onExpandedChange = { expandedCat = !expandedCat }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCat,
                        onDismissRequest = { expandedCat = false }
                    ) {
                        categoryList.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    category = option
                                    expandedCat = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = pricePerUnit,
                        onValueChange = { pricePerUnit = it },
                        label = { Text("Price per Unit (₹)*") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("form_med_price"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    OutlinedTextField(
                        value = stockQuantity,
                        onValueChange = { stockQuantity = it },
                        label = { Text("Stock Quantity*") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("form_med_stock"),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Unit Dropdown
                var expandedUnit by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedUnit,
                    onExpandedChange = { expandedUnit = !expandedUnit }
                ) {
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("Unit Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUnit) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedUnit,
                        onDismissRequest = { expandedUnit = false }
                    ) {
                        unitList.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    unit = option
                                    expandedUnit = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = defaultDosage,
                    onValueChange = { defaultDosage = it },
                    label = { Text("Default Dosage (e.g., 1-0-1)") },
                    modifier = Modifier.fillMaxWidth().testTag("form_med_dosage"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Anupanam
                var expandedAnu by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedAnu,
                    onExpandedChange = { expandedAnu = !expandedAnu }
                ) {
                    OutlinedTextField(
                        value = defaultAnupanam,
                        onValueChange = { defaultAnupanam = it },
                        label = { Text("Default Anupanam (Vehicle)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAnu) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedAnu,
                        onDismissRequest = { expandedAnu = false }
                    ) {
                        anupanamList.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    defaultAnupanam = option
                                    expandedAnu = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Health Benefits / Uses Description") },
                    modifier = Modifier.fillMaxWidth().testTag("form_med_desc"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val stock = stockQuantity.toIntOrNull() ?: 0
                        val price = pricePerUnit.toDoubleOrNull() ?: 0.0
                        if (name.isNotBlank() && price >= 0) {
                            onSave(
                                Medicine(
                                    id = editingMedicine?.id ?: 0,
                                    name = name,
                                    category = category,
                                    stockQuantity = stock,
                                    unit = unit,
                                    pricePerUnit = price,
                                    defaultDosage = defaultDosage,
                                    defaultAnupanam = defaultAnupanam,
                                    timing = timing,
                                    description = description
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("form_save_medicine"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Save Medicine Stock", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
