package com.example.fitgod.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitgod.data.remote.model.IstilahDto
import com.example.fitgod.ui.screen.detail.DetailIstilahScreen
import com.example.fitgod.ui.screen.form.AddIstilahScreen
import com.example.fitgod.ui.screen.form.EditIstilahScreen
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

    // state untuk page tambah & edit
    var isAddingPage by remember { mutableStateOf(false) }
    var isEditingPage by remember { mutableStateOf(false) }
    var editingIstilah by remember { mutableStateOf<IstilahDto?>(null) } // Restored
    var showEditDialog by remember { mutableStateOf(false) }

    // state untuk detail popup
    var showDetailDialog by remember { mutableStateOf(false) } // Restored
    var selectedIstilah by remember { mutableStateOf<IstilahDto?>(null) } // Restored

    var searchText by rememberSaveable { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    // Dynamic Tabs based on Categories
    val categories = remember(istilahList) {
        istilahList.map { it.kategori }.distinct().sorted()
    }
    val tabs = listOf("All") + categories

    // load data awal
    LaunchedEffect(Unit) {
        istilahViewModel.loadIstilah()
    }

    val filteredIstilah = remember(istilahList, searchText, selectedTab, tabs) {
        var result = istilahList
        
        // 1. Filter by Search
        if (searchText.isNotBlank()) {
            val q = searchText.lowercase()
           result = result.filter { istilah ->
                istilah.namaIstilah.lowercase().contains(q) ||
                        istilah.kategori.lowercase().contains(q) ||
                        istilah.deskripsi.lowercase().contains(q)
            }
        }
        
        // 2. Filter by Tab
        if (selectedTab > 0 && selectedTab < tabs.size) {
            val selectedCategory = tabs[selectedTab]
            result = result.filter { it.kategori.equals(selectedCategory, ignoreCase = true) }
        } else {
             // Tab 0 is "All", ensure no accidental out of bounds if tabs change
             // No filtering needed
        }
        
        result
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // White
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 24.dp, end = 24.dp)
            ) {
                // Top Row: Avatar & Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile + Name Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Profile Avatar Placeholder
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            // First letter of username or default
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = authViewModel.getUsername()?.take(1)?.uppercase() ?: "U",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                             Text(
                                text = "Hello,",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = authViewModel.getUsername() ?: "Guest",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                         // Removed unused Bookmark icon if not functional, or kept as requested?
                         // User said "function... delete it". 
                         // Share icon is there. Let's keep Share and Logout.
                        IconButton(onClick = { /* TODO: Implement Share */ }) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.onBackground)
                        }
                        IconButton(onClick = { 
                            authViewModel.logout()
                            onLogout()
                         }) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Title "FitGod"
                Text(
                    text = "FitGod", 
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Scrollable Tabs if many categories
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                     items(tabs.size) { index ->
                        val title = tabs[index]
                         Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { selectedTab = index }
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.tertiary
                                )
                            )
                            if (selectedTab == index) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    modifier = Modifier
                                        .width(32.dp)
                                        .height(2.dp),
                                    color = MaterialTheme.colorScheme.onBackground
                                ) {}
                            }
                        }
                     }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        floatingActionButton = {
            if (!isAddingPage && !isEditingPage) {
                FloatingActionButton(
                    onClick = {
                        editingIstilah = null
                        isAddingPage = true
                    },
                    containerColor = MaterialTheme.colorScheme.onBackground, // Black FAB
                    contentColor = MaterialTheme.colorScheme.background // White Icon
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah")
                }
            }
        }
    ) { innerPadding ->

        when {
            // ================= PAGE TAMBAH =================
            isAddingPage -> {
                 Box(modifier = Modifier.padding(innerPadding)) {
                     AddIstilahScreen(
                        onSave = { n, k, d ->
                            istilahViewModel.addIstilah(n, k, d)
                            isAddingPage = false
                        },
                        onCancel = { isAddingPage = false }
                     )
                 }
            }

            // ================= PAGE EDIT =================
            isEditingPage && editingIstilah != null -> {
                Box(modifier = Modifier.padding(innerPadding)) {
                    EditIstilahScreen(
                        initialNama = editingIstilah!!.namaIstilah,
                        initialKategori = editingIstilah!!.kategori,
                        initialDeskripsi = editingIstilah!!.deskripsi,
                        onSave = { n, k, d ->
                            istilahViewModel.updateIstilah(editingIstilah!!.idIstilah, n, k, d)
                            isEditingPage = false
                            editingIstilah = null
                        },
                        onCancel = { isEditingPage = false; editingIstilah = null }
                    )
                }
            }

            // ================= HALAMAN LIST ("New Workouts") =================
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Search Bar (Restored)
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        label = { Text("Search Terms...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant, // Grey background
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )

                    Text(
                        text = "Recent Terms", // Changed from New Workouts
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
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
                
                // Dialogs
                if (showDetailDialog && selectedIstilah != null) {
                        DetailIstilahScreen(
                            istilah = selectedIstilah!!,
                            onEdit = {
                                showDetailDialog = false
                                editingIstilah = selectedIstilah // Set editing item
                                isEditingPage = true // Go to full page edit
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
                // Removed Dialog(showEditDialog) block as we now use isEditingPage
            }
        }
    }
}

@Composable
private fun IstilahItem(item: IstilahDto, onClick: () -> Unit) {
    // Simple Card Look (No Image)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // Greyish bg
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.namaIstilah,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.kategori.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                 Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.deskripsi,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.background, androidx.compose.foundation.shape.CircleShape)
            ) {
                 Icon(
                     imageVector = Icons.Default.Add, 
                     contentDescription = "Detail",
                     tint = MaterialTheme.colorScheme.onBackground,
                     modifier = Modifier.size(16.dp)
                 )
            }
        }
    }
}
