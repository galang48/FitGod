package com.example.fitgod.ui.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitgod.data.remote.model.IstilahDto

@Composable
fun DetailIstilahScreen(
    istilah: IstilahDto,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            Button(onClick = onEdit) {
                Text("Edit")
            }
        },
        dismissButton = {
            Column {
                TextButton(onClick = onDelete) {
                    Text("Hapus")
                }
                TextButton(onClick = onClose) {
                    Text("Tutup")
                }
            }
        },
        title = { Text(text = istilah.namaIstilah, fontWeight = FontWeight.SemiBold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Kategori: ${istilah.kategori}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Deskripsi:")
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = istilah.deskripsi)
            }
        }
    )
}
