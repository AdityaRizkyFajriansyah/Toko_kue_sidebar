package com.example.toko_kue.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.example.toko_kue.data.model.Produk
import com.example.toko_kue.viewmodel.BahanViewModel
import com.example.toko_kue.viewmodel.ProdukViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    bahanViewModel: BahanViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    produkViewModel: ProdukViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateToDaftarBahan: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showSheet by remember { mutableStateOf(false) }

    // variabel untuk show pop up input bahan dan input produk
    var showInputBahan by remember { mutableStateOf(false) }
    var showInputProduk by remember { mutableStateOf(false)}

    // variabel untuk daft
    var daftarbahan by remember { mutableStateOf(false)}

    // List produk dinamis
    var produkList by remember { mutableStateOf(listOf<Produk>()) }
    var produkEdit by remember { mutableStateOf<Produk?>(null)}

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))

                // Tombol untuk buka popup Input Bahan
                Button(
                    onClick = {
                        onNavigateToDaftarBahan()
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Bahan")
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        showInputProduk = true
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Produk")
                }
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

                // ambil dari Produk View Model
                produkViewModel.produkList.forEach { produk->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ){
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Column{
                                Text(
                                    text = produk.nama,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "Harga: Rp.${produk.hargaJual}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )

                                Row{
                                    IconButton(onClick = {
                                        // TODO: Edit Produk
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                                    }
                                    IconButton(onClick = {
                                        //TODO: hapus produk
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Hapus")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Popup merah
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
                    onClick = { showInputBahan = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Input Bahan", color = Color.Black)
                }
                Button(
                    onClick = { showInputProduk = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Input Produk", color = Color.Black)
                }
            }
        }
    }
    
    // Popup Input Bahan
    if (showInputBahan) {
        InputBahanDialog(
            onDismiss = { showInputBahan = false },
            onSave = { nama, jumlah, harga ->
                println("Bahan: $nama, Jumlah: $jumlah, Harga: $harga")
            }
        )
    }

    // pop up untuk input produk
    if(showInputProduk){
        InputProdukDialog(
            bahanViewModel = bahanViewModel,
            produkViewModel = produkViewModel,
            onDismiss = { showInputProduk = false},
            onNavigateToInputProdukScreen = {
                showInputProduk = false
                navController.navigate("inputProduk")
            }
        )
    }

    // popup edit produk
    if(produkEdit != null){
        EditProdukDialog(
            produk = produkEdit!!,
            onDismiss = { produkEdit = null },
            onSave = { updatedProduk ->
                produkList = produkList.map {
                    if(it.id == updatedProduk.id) updatedProduk else it
                }
                produkEdit = null
            }
        )
    }
}

@Composable
fun InputBahanDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }

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
                    label = { Text("Nama Bahan") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = jumlah,
                    onValueChange = { jumlah = it },
                    label = { Text("Jumlah") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = harga,
                    onValueChange = { harga = it },
                    label = { Text("Harga") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        onSave(nama, jumlah, harga)
                        onDismiss()
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

@Composable
fun InputProdukDialog(
    onDismiss: () -> Unit,
    bahanViewModel: BahanViewModel,
    produkViewModel: ProdukViewModel,
    onNavigateToInputProdukScreen: () -> Unit
){
    var namaProduk by remember { mutableStateOf("")}
    var hargaJual by remember { mutableStateOf("")}
    var jumlahJadi by remember { mutableStateOf("")}

    // variable input bahan yang dipakai
    var namaBahan by remember { mutableStateOf("")}
    var jumlahBahan by remember { mutableStateOf("")}

    Dialog(onDismissRequest = onDismiss){
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color(0xFFE0E0E0),
            tonalElevation = 6.dp
        ){
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                OutlinedTextField(
                    value = namaProduk,
                    onValueChange = { namaProduk = it},
                    label = { Text("Input produk apa?")},
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = hargaJual,
                    onValueChange = { hargaJual = it},
                    label = { Text("Harga Jual per Pcs")},
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = jumlahJadi,
                    onValueChange = { jumlahJadi = it},
                    label = { Text("Jumlah Jadi")},
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        // kurangi stok bahan(jaga jaga)
                        bahanViewModel.kurangiBahan(
                            namaBahan,
                            jumlahBahan.toDoubleOrNull()?: 0.0
                        )

                        // Simpan produk
                        val produk = Produk(
                            id = produkViewModel.produkList.size + 1,
                            nama = namaProduk,
                            hargaJual = hargaJual.toDoubleOrNull()?: 0.0,
                            jumlahJadi = jumlahJadi.toIntOrNull()?: 0,
                            bahanDipakai = listOf(namaBahan to (jumlahBahan.toDoubleOrNull()?: 0.0))
                        )

                        produkViewModel.tambahProduk(produk)

                        onNavigateToInputProdukScreen()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ){
                    Text("Simpan", color = Color.White)
                }
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
    var nama by remember { mutableStateOf(produk.nama)}

    Dialog(onDismissRequest = onDismiss){
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color(0xFFE0E0E0)
        ){
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it},
                    label = { Text("Nama Produk") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { onSave(produk.copy(nama = nama)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ){
                    Text("Simpan", color = Color.White)
                }
            }
        }
    }
}