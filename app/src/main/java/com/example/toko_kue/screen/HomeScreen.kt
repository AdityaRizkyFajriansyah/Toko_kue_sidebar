package com.example.toko_kue.screen

import android.app.Dialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.toko_kue.data.model.Produk
import com.example.toko_kue.viewmodel.BahanViewModel
import com.example.toko_kue.viewmodel.produkViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    bahanViewModel: BahanViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    produkViewModel: produkViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateToDaftarBahan: () -> Unit = {},
    onNavigateToInputProdukScreen: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))

    var showInputBahan by remember { mutableStateOf(false)}
    var showSheet by remember { mutableStateOf(false) }
    var produkEdit by remember { mutableStateOf<Produk?>(null) }


    // ✅ Ambil data produk dari backend lewat ViewModel
    val produkList by produkViewModel.produkList.collectAsState()
    LaunchedEffect(Unit) {
        produkViewModel.fetchProduk()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onNavigateToDaftarBahan()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) { Text("Bahan") }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            kotlinx.coroutines.delay(250)
                            onNavigateToInputProdukScreen()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) { Text("Input Produk") }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Home Screen") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showSheet = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
            ) {
                Text(
                    "Daftar Produk",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 16.dp),
                    style = MaterialTheme.typography.titleLarge
                )

                if (produkList.isEmpty()) {
                    Text(
                        "Belum ada data produk.",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.DarkGray
                    )
                } else {
                    produkList.forEach { produk ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                                        text = produk.nama,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "Harga: Rp${formatter.format(produk.harga ?: BigDecimal.ZERO)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.DarkGray
                                    )
                                    Text(
                                        text = "Jumlah: ${formatter.format(produk.jumlah ?: BigDecimal.ZERO)} pcs",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.DarkGray
                                    )
                                }

                                Row {
                                    IconButton(onClick = { produkEdit = produk }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                                    }
                                    IconButton(onClick = {
                                        produk.id?.let { produkViewModel.hapusProduk(it) }
                                    }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Hapus",
                                            tint = Color.Black
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

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color.Red
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        onNavigateToInputProdukScreen()
                        showSheet = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Input Produk", color = Color.Black)
                }

                Button(
                    onClick = {
                        showSheet = false
                        showInputBahan = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ){
                    Text("Input Bahan", color = Color.Black)
                }
            }
        }

    }

    if (showInputBahan) {
        InputBahanDialog(
            onDismiss = { showInputBahan = false },
            onSave = { nama, jumlah, harga ->
                bahanViewModel.tambahBahan(
                    nama = nama,
                    jumlah = jumlah,
                    harga = harga
                )
                showInputBahan = false
            }
        )
    }

    if (produkEdit != null) {
        EditProdukDialog(
            produk = produkEdit!!,
            onDismiss = { produkEdit = null },
            onSave = { updated ->
                // Contoh update lokal (kalau mau update ke backend, tambahkan fungsi PUT di ApiService)
                produkViewModel.produkList.value.toMutableList().apply {
                    removeIf { it.id == updated.id }
                    add(updated)
                    produkViewModel.fetchProduk() // refresh dari backend
                }
                produkEdit = null
            }
        )
    }
}

@Composable
fun InputBahanDialog(
    onDismiss: () -> Unit,
    onSave: (String, BigDecimal, BigDecimal) -> Unit
){
    var nama by remember { mutableStateOf("")}
    var jumlah by remember { mutableStateOf("")}
    var harga by remember { mutableStateOf("")}

    Dialog(onDismissRequest = onDismiss){
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.White,
            tonalElevation = 6.dp
        ){
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text("Input Bahan Baru", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(value = nama, onValueChange = {nama = it}, label = { Text("Nama Bahan")})
                OutlinedTextField(value = jumlah, onValueChange = { jumlah = it}, label = { Text(("Jumlah (dr/ml)"))})
                OutlinedTextField(value = harga, onValueChange = { harga = it }, label = { Text("Harga Total")})

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", color = Color.Gray)
                    }

                    Button(onClick = {
                        if(nama.isNotEmpty() && jumlah.isNotEmpty() && harga.isNotEmpty()){
                            onSave(
                                nama, jumlah.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                                harga.toBigDecimalOrNull() ?: BigDecimal.ZERO
                            )
                        }
                    }){
                        Text("Simpan")
                    }
                }
            }

            Column (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){

            }
        }

    }
}

@Composable
fun EditProdukDialog(
    produk: Produk,
    onDismiss: () -> Unit,
    onSave: (Produk) -> Unit
) {
    var nama by remember { mutableStateOf(produk.nama) }
    var hargaText by remember { mutableStateOf(produk.harga?.toPlainString() ?: "0") }
    var jumlahText by remember { mutableStateOf(produk.jumlah?.toPlainString() ?: "0") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color(0xFFE0E0E0)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Produk") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = hargaText,
                    onValueChange = { hargaText = it },
                    label = { Text("Harga per pcs") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = jumlahText,
                    onValueChange = { jumlahText = it },
                    label = { Text("Jumlah") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        onSave(
                            produk.copy(
                                nama = nama,
                                harga = hargaText.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                                jumlah = jumlahText.toBigDecimalOrNull() ?: BigDecimal.ZERO
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Simpan", color = Color.White)
                }
            }
        }
    }
}