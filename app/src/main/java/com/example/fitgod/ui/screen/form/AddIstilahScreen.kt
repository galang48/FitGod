package com.example.fitgod.ui.screen.form

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddIstilahScreen(
    onSave: (String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Nama istilah") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = kategori,
            onValueChange = { kategori = it },
            label = { Text("Kategori") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = deskripsi,
            onValueChange = { deskripsi = it },
            label = { Text("Deskripsi") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text("Batal")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (nama.isNotBlank() && kategori.isNotBlank() && deskripsi.isNotBlank()) {
                        onSave(nama, kategori, deskripsi)
                    }
                }
            ) {
                Text("Simpan")
            }
        }
    }
}
