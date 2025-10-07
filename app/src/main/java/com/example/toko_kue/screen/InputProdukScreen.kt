package com.example.toko_kue.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toko_kue.data.model.Bahan
import com.example.toko_kue.data.model.Produk
import com.example.toko_kue.viewmodel.BahanViewModel
import com.example.toko_kue.viewmodel.ProdukViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputProdukScreen(
    navController: NavController,
    bahanViewModel: BahanViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    produkViewModel: ProdukViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit,
    onSelesai: () -> Unit
) {
    // 🔹 Ambil produk terakhir (yang sudah diinput di popup sebelumnya)
    val produkTerakhir = produkViewModel.produkList.lastOrNull()

    // 🔹 State untuk dropdown dan input bahan
    var expanded by remember { mutableStateOf(false) }
    var selectedBahan by remember { mutableStateOf("") }
    var jumlahDigunakan by remember { mutableStateOf("") }

    // 🔹 Daftar bahan dari ViewModel
    val bahanList = bahanViewModel.bahanList

    // 🔹 List bahan yang dipakai oleh produk ini
    var bahanDipakai by remember { mutableStateOf(mutableListOf<Pair<String, Double>>()) }

    // 🔹 State laba
    var laba by remember { mutableStateOf<Double?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input Produk ambil bahan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFB2DFDB))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ✅ Info produk yang sudah diinput sebelumnya
            produkTerakhir?.let {
                Text("Nama Produk: ${it.nama}", style = MaterialTheme.typography.titleLarge)
                Text("Harga Jual per pcs: Rp${it.hargaJual}")
                Text("Jumlah Jadi: ${it.jumlahJadi} pcs")
            } ?: Text("Belum ada produk disimpan sebelumnya")

            Divider(color = Color.Gray, thickness = 1.dp)

            // ✅ Dropdown bahan baku
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedBahan,
                    onValueChange = {},
                    label = { Text("Pilih Bahan Baku") },
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    bahanList.forEach { bahan ->
                        DropdownMenuItem(
                            text = { Text(bahan.nama) },
                            onClick = {
                                selectedBahan = bahan.nama
                                expanded = false
                            }
                        )
                    }
                }
            }

            // ✅ Input jumlah bahan yang digunakan
            OutlinedTextField(
                value = jumlahDigunakan,
                onValueChange = { jumlahDigunakan = it },
                label = { Text("Jumlah bahan digunakan (gram/ml)") },
                modifier = Modifier.fillMaxWidth()
            )

            // ✅ Tombol simpan bahan
            Button(
                onClick = {
                    if (selectedBahan.isNotEmpty() && jumlahDigunakan.isNotEmpty()) {
                        val jumlah = jumlahDigunakan.toDoubleOrNull() ?: 0.0
                        bahanViewModel.kurangiBahan(selectedBahan, jumlah)
                        bahanDipakai.add(selectedBahan to jumlah)
                        jumlahDigunakan = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
            ) {
                Text("Simpan Bahan", color = Color.White)
            }

            // ✅ Tampilkan bahan yang digunakan
            if (bahanDipakai.isNotEmpty()) {
                Text("Bahan digunakan:", style = MaterialTheme.typography.titleMedium)
                LazyColumn {
                    items(bahanDipakai.size) { index ->
                        val (nama, jumlah) = bahanDipakai[index]
                        Text("- $nama : $jumlah")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                // ✅ Tombol hitung laba
                Button(
                    onClick = {
                        if (produkTerakhir != null) {
                            val totalModal = bahanDipakai.sumOf { (nama, jumlah) ->
                                val bahan = bahanList.find { it.nama == nama }
                                val hargaSatuan = bahan?.harga ?: 0.0
                                (jumlah / 1000.0) * hargaSatuan // konversi gram → kg
                            }
                            val omzet = produkTerakhir.hargaJual * produkTerakhir.jumlahJadi
                            laba = omzet - totalModal
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688))
                ) {
                    Text("Hitung Laba", color = Color.White)
                }

                Button(
                    onClick = { navController.popBackStack() }, // balik ke HomeScreen
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
                ) {
                    Text("Home", color = Color.White)
                }
            }



            // ✅ Tampilkan hasil laba
            laba?.let {
                Text(
                    "Laba bersih: Rp${"%.0f".format(it)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = if (it >= 0) Color(0xFF2E7D32) else Color.Red
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Selesai → kembali ke HomeScreen
            Button(
                onClick = {
                    // Update produk terakhir dengan bahan dan laba
                    val produk = produkTerakhir?.copy(bahanDipakai = bahanDipakai.toList())
                    if (produk != null) {
                        produkViewModel.produkList.removeAt(produkViewModel.produkList.lastIndex)
                        produkViewModel.tambahProduk(produk)
                    }
                    onSelesai()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
            ) {
                Text("Selesai", color = Color.White)
            }
        }
    }
}
