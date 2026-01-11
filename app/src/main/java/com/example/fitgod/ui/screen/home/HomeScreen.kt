package com.example.fitgod.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitgod.data.remote.model.IstilahDto
import com.example.fitgod.ui.screen.detail.DetailIstilahScreen
import com.example.fitgod.ui.screen.form.AddIstilahScreen
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

    // EDIT dialog
    var showFormDialog by remember { mutableStateOf(false) }
    var editingIstilah by remember { mutableStateOf<IstilahDto?>(null) }

    // DETAIL popup
    var showDetailDialog by remember { mutableStateOf(false) }
    var selectedIstilah by remember { mutableStateOf<IstilahDto?>(null) }

    // PAGE tambah istilah
    var isAddingPage by remember { mutableStateOf(false) }

    var searchText by rememberSaveable { mutableStateOf("") }

    // load data awal
    LaunchedEffect(Unit) {
        istilahViewModel.loadIstilah()
    }

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
                title = {
                    Text(
                        text = if (isAddingPage)
                            "Tambah Istilah"
                        else
                            "FitGod - Kamus Istilah Gym"
                    )
                },
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
            if (!isAddingPage) {
                FloatingActionButton(
                    onClick = {
                        editingIstilah = null
                        isAddingPage = true   // buka halaman tambah
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah istilah"
                    )
                }
            }
        }
    ) { innerPadding ->

        if (isAddingPage) {
            // ================== HALAMAN TAMBAH ==================
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AddIstilahScreen(
                    onSave = { nama, kategori, deskripsi ->
                        // DI SINI PAKAI PARAMETER NAMA, BUKAN namaIstilah
                        istilahViewModel.addIstilah(
                            nama,
                            kategori,
                            deskripsi
                        )
                        isAddingPage = false
                    },
                    onCancel = {
                        isAddingPage = false
                    }
                )
            }
        } else {
            // ================== HALAMAN LIST ==================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Cari istilah gym") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

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
    }

    // ================== EDIT POPUP ==================
    if (showFormDialog) {
        IstilahFormScreen(
            title = if (editingIstilah == null) "Tambah Istilah" else "Edit Istilah",
            initialNama = editingIstilah?.namaIstilah ?: "",
            initialKategori = editingIstilah?.kategori ?: "",
            initialDeskripsi = editingIstilah?.deskripsi ?: "",
            onSave = { nama, kategori, deskripsi ->
                if (editingIstilah == null) {
                    // fallback, jarang terpakai
                    istilahViewModel.addIstilah(
                        nama,
                        kategori,
                        deskripsi
                    )
                } else {
                    // DI SINI JUGA: pakai nama, bukan namaIstilah
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

    // ================== DETAIL POPUP ==================
    if (showDetailDialog && selectedIstilah != null) {
        DetailIstilahScreen(
            istilah = selectedIstilah!!,
            onEdit = {
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
