package com.example.fitgod.ui.screen.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * PAGE untuk TAMBAH istilah (kosong dari awal)
 */
@Composable
fun AddIstilahScreen(
    onSave: (String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }

    IstilahFormContent(
        titleLabel = "Tambah Istilah",
        nama = nama,
        onNamaChange = { nama = it },
        kategori = kategori,
        onKategoriChange = { kategori = it },
        deskripsi = deskripsi,
        onDeskripsiChange = { deskripsi = it },
        onSave = { onSave(nama, kategori, deskripsi) },
        onCancel = onCancel
    )
}

/**
 * PAGE untuk EDIT istilah (terisi data lama)
 */
@Composable
fun EditIstilahScreen(
    initialNama: String,
    initialKategori: String,
    initialDeskripsi: String,
    onSave: (String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var nama by remember { mutableStateOf(initialNama) }
    var kategori by remember { mutableStateOf(initialKategori) }
    var deskripsi by remember { mutableStateOf(initialDeskripsi) }

    IstilahFormContent(
        titleLabel = "Edit Istilah",
        nama = nama,
        onNamaChange = { nama = it },
        kategori = kategori,
        onKategoriChange = { kategori = it },
        deskripsi = deskripsi,
        onDeskripsiChange = { deskripsi = it },
        onSave = { onSave(nama, kategori, deskripsi) },
        onCancel = onCancel
    )
}

/**
 * Konten form yang dipakai Add & Edit (biar nggak duplikat layout).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IstilahFormContent(
    titleLabel: String,
    nama: String,
    onNamaChange: (String) -> Unit,
    kategori: String,
    onKategoriChange: (String) -> Unit,
    deskripsi: String,
    onDeskripsiChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("Latihan", "Alat", "Nutrisi", "Otot")

    // Full Page Layout (White)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // White
            .padding(24.dp)
            .padding(top = 16.dp) // Extra top padding
    ) {
        // Simple Back/Title Header
        Row(verticalAlignment = Alignment.CenterVertically) {
             // Optional Back Icon could be here, but onCancel (TextButton) is at bottom.
             // We can just have large Title.
            Text(
                text = titleLabel.uppercase(),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Inputs with Clean styling
        OutlinedTextField(
            value = nama,
            onValueChange = onNamaChange,
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = kategori,
                onValueChange = {}, // ReadOnly
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(), // Required modifier
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onKategoriChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = deskripsi,
            onValueChange = onDeskripsiChange,
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp),
            shape = RoundedCornerShape(12.dp),
            minLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.weight(1f)) // Push buttons to bottom

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancel) {
                Text(text = "CANCEL", color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onSave,
                shape = RoundedCornerShape(50),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White) 
            ) {
                Text(text = "SAVE", color = Color.White, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
        }
    }
}
