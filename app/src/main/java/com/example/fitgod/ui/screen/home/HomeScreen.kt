package com.example.fitgod.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitgod.data.remote.model.IstilahDto
import com.example.fitgod.ui.screen.detail.DetailIstilahScreen
import com.example.fitgod.ui.screen.form.IstilahFormScreen
import com.example.fitgod.util.UiState
import com.example.fitgod.viewmodel.AuthViewModel
import com.example.fitgod.viewmodel.IstilahViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    istilahViewModel: IstilahViewModel,
    onLogout: () -> Unit
) {
    val istilahList by istilahViewModel.istilahList.collectAsStateWithLifecycle()
    val operationState by istilahViewModel.operationState.collectAsStateWithLifecycle()

    var showFormDialog by remember { mutableStateOf(false) }
    var editingIstilah by remember { mutableStateOf<IstilahDto?>(null) }

    var showDetailDialog by remember { mutableStateOf(false) }
    var selectedIstilah by remember { mutableStateOf<IstilahDto?>(null) }

    var searchText by rememberSaveable { mutableStateOf("") }

    // LOAD DATA PERTAMA KALI
    LaunchedEffect(Unit) {
        istilahViewModel.loadIstilah()
    }

    // Filter di sisi client (tanpa request lagi ke API)
    val filteredIstilah = remember(istilahList, searchText) {
        if (searchText.isBlank()) {
            istilahList
        } else {
            val q = searchText.lowercase()
            istilahList.filter { istilah ->
                istilah.namaIstilah.lowercase().contains(q) ||
                        istilah.kategori.lowercase().contains(q) ||
                        istilah.deskripsi.lowercase().contains(q)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "FitGod - Kamus Istilah Gym") },
                actions = {
                    TextButton(onClick = {
                        authViewModel.logout()
                        onLogout()
                    }) {
                        Text(text = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // mode tambah
                    editingIstilah = null
                    showFormDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah istilah"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Search field
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Cari istilah gym") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tampilkan error dari operasi add / update / delete
            val errorMessage = (operationState as? UiState.Error)?.message
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // List istilah
            if (filteredIstilah.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada istilah. Tambahkan dengan tombol +",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredIstilah) { item ->
                        IstilahItem(
                            item = item,
                            onClick = {
                                selectedIstilah = item
                                showDetailDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Dialog tambah / edit
    if (showFormDialog) {
        IstilahFormScreen(
            title = if (editingIstilah == null) "Tambah Istilah" else "Edit Istilah",
            initialNama = editingIstilah?.namaIstilah ?: "",
            initialKategori = editingIstilah?.kategori ?: "",
            initialDeskripsi = editingIstilah?.deskripsi ?: "",
            onSave = { nama, kategori, deskripsi ->
                if (editingIstilah == null) {
                    // TAMBAH – pakai urutan parameter biasa
                    istilahViewModel.addIstilah(
                        nama,
                        kategori,
                        deskripsi
                    )
                } else {
                    // UPDATE – kirim id + data baru
                    istilahViewModel.updateIstilah(
                        editingIstilah!!.idIstilah,
                        nama,
                        kategori,
                        deskripsi
                    )
                }
                showFormDialog = false
                editingIstilah = null
            },
            onDismiss = {
                showFormDialog = false
                editingIstilah = null
            }
        )
    }

    // Dialog detail (di sini saja ada Edit & Hapus)
    if (showDetailDialog && selectedIstilah != null) {
        DetailIstilahScreen(
            istilah = selectedIstilah!!,
            onEdit = {
                // masuk mode edit dengan data istilah terpilih
                editingIstilah = selectedIstilah
                showDetailDialog = false
                showFormDialog = true
            },
            onDelete = {
                istilahViewModel.deleteIstilah(selectedIstilah!!.idIstilah)
                showDetailDialog = false
                selectedIstilah = null
            },
            onClose = {
                showDetailDialog = false
                selectedIstilah = null
            }
        )
    }
}

@Composable
private fun IstilahItem(
    item: IstilahDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(all = 12.dp)) {
            Text(
                text = item.namaIstilah,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.deskripsi,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
