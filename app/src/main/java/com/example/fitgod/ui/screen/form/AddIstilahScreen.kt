package com.example.fitgod.ui.screen.form

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = titleLabel)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nama,
            onValueChange = onNamaChange,
            label = { Text("Nama istilah") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = kategori,
            onValueChange = onKategoriChange,
            label = { Text("Kategori") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = deskripsi,
            onValueChange = onDeskripsiChange,
            label = { Text("Deskripsi") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancel) {
                Text(text = "Batal")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onSave) {
                Text(text = "Simpan")
            }
        }
    }
}
