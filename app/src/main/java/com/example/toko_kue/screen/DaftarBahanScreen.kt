package com.example.toko_kue.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.toko_kue.viewmodel.BahanViewModel
import com.example.toko_kue.data.model.Bahan
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarBahanScreen(
    bahanViewModel: BahanViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit
) {
    // Ambil data dari ViewModel (diambil dari Retrofit)
    val bahanList by bahanViewModel.bahanList.collectAsState()

    // Jalankan GET ke API saat screen dibuka
    LaunchedEffect(Unit) {
        bahanViewModel.fetchBahan()
    }

    // State untuk dialog edit
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedBahan by remember { mutableStateOf<Bahan?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Handle tombol back HP
    BackHandler { onBack() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Side Menu",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                HorizontalDivider()
                Text(
                    "Home",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            scope.launch { drawerState.close() }
                            onBack()
                        }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Daftar Bahan Baku") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // TODO: Tambahkan dialog tambah bahan di sini nanti
                    },
                    containerColor = Color(0xFF00796B)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Bahan", tint = Color.White)
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color(0xFFB2DFDB))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Daftar Bahan Baku",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                if (bahanList.isEmpty()) {
                    Text("Belum ada data bahan.", color = Color.DarkGray)
                } else {
                    bahanList.forEach { bahan ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFD9D9D9))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        bahan.nama,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text("Stok: ${bahan.jumlah} gr/ml", color = Color.DarkGray)
                                    Text("Harga: Rp${bahan.harga}", color = Color.DarkGray)
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    IconButton(onClick = {
                                        selectedBahan = bahan
                                        showEditDialog = true
                                    }) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = Color(0xFF00796B)
                                        )
                                    }
                                    IconButton(onClick = {
                                        bahan.id?.let { bahanViewModel.hapusBahan(it) }
                                    }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Hapus",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog edit bahan
    if (showEditDialog && selectedBahan != null) {
        EditBahanDialog(
            bahan = selectedBahan!!,
            onDismiss = { showEditDialog = false },
            onSave = { updated ->
                bahanViewModel.updateBahan(updated)
                showEditDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBahanDialog(
    bahan: Bahan,
    onDismiss: () -> Unit,
    onSave: (Bahan) -> Unit
) {
    var nama by remember { mutableStateOf(bahan.nama) }
    var stok by remember { mutableStateOf(bahan.jumlah.toString()) }
    var harga by remember { mutableStateOf(bahan.harga.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Bahan") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = nama, onValueChange = { nama = it }, label = { Text("Nama Bahan") })
                OutlinedTextField(value = stok, onValueChange = { stok = it }, label = { Text("Stok (gr/ml)") })
                OutlinedTextField(value = harga, onValueChange = { harga = it }, label = { Text("Harga") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    bahan.copy(
                        nama = nama,
                        jumlah = stok.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                        harga = harga.toBigDecimalOrNull() ?: BigDecimal.ZERO
                    )
                )
            }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}
