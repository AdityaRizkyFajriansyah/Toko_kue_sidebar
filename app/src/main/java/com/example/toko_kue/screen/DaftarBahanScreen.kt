package com.example.toko_kue.screen

import android.graphics.Paint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import com.example.toko_kue.viewmodel.BahanViewModel
import com.example.toko_kue.data.model.Bahan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarBahanScreen(
    bahanViewModel: BahanViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit
) {
    // ambil list bahan dari viewModel
    val bahanList by bahanViewModel.bahanList.collectAsState()

    LaunchedEffect(Unit) {
        bahanViewModel.fetchBahan()
    }

    // panggil edit dialog
    var showEditDialog by remember { mutableStateOf(false)}
    var selectedBahan by remember { mutableStateOf<Bahan?>(null)}

    // input field
    var nama by remember { mutableStateOf("") }
    var stok by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("")}

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    // handel tombol back hp
    BackHandler {
        onBack()
    }

    // side bar
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

                Text(
                    "Report Harian",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                Text(
                    "Report Bulanan",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
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
                FloatingActionButton(onClick = {
                    if (nama.isNotEmpty() && stok.isNotEmpty() && harga.isNotEmpty()) {
                        bahanViewModel.tambahBahan(
                            Bahan(
                                nama = nama,
                                jumlah = stok.toDouble(),
                                harga = harga.toDouble()
                            )
                        )
                        nama = ""
                        stok = ""
                        harga = ""
                    }

                }) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Bahan")
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
                                .padding(vertical = 8.dp)
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
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text("Stok: ${bahan.jumlah} gr/ml", color = Color.DarkGray)
                                    Text("Harga: Rp.${bahan.harga}", color = Color.DarkGray)
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    IconButton(onClick = {
                                        selectedBahan = bahan
                                        showEditDialog = true
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

    if(showEditDialog && selectedBahan != null){
        EditBahanDialog(
            bahan = selectedBahan!!,
            onDismiss = {showEditDialog = false},
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
                        jumlah = stok.toDoubleOrNull() ?: 0.0,
                        harga = harga.toDoubleOrNull() ?: 0.0
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