package com.example.toko_kue.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toko_kue.data.model.Bahan
import com.example.toko_kue.data.model.BahanPakaiRequest
import com.example.toko_kue.viewmodel.BahanViewModel
import com.example.toko_kue.viewmodel.produkViewModel
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputBahanProdukScreen(
    produkId: String,
    bahanViewModel: BahanViewModel,
    produkViewModel: produkViewModel,
    navController: NavController
) {
    val bahanList by bahanViewModel.bahanList.collectAsState()
    val selectedBahan = remember { mutableStateListOf<BahanPakaiRequest>() }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        bahanViewModel.fetchBahan()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input Bahan Produk") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Tambahkan bahan yang dipakai untuk produk ini:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (bahanList.isEmpty()) {
                item {
                    Text("Belum ada data bahan baku.", color = Color.Gray)
                }
            } else {
                items(bahanList) { bahan ->
                    BahanItemCard(
                        bahan = bahan,
                        onTambah = { jumlahDipakai ->
                            val bahanPakai = BahanPakaiRequest(
                                id = null,
                                produkId = produkId,
                                bahanBakuId = bahan.id ?: "",
                                bahanBakuNama = bahan.nama,
                                jumlahDipakai = jumlahDipakai
                            )
                            selectedBahan.add(bahanPakai)
                        }
                    )
                }
            }

            // 🔽 Tambahkan tombol Simpan Semua di bawah daftar, ikut di-scroll
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        isSaving = true
                        selectedBahan.forEach {
                            produkViewModel.tambahBahanKeProduk(it)
                        }
                        isSaving = false
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text(
                        if (isSaving) "Menyimpan..." else "Simpan Semua",
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun BahanItemCard(
    bahan: Bahan,
    onTambah: (BigDecimal) -> Unit
) {
    var jumlahText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = bahan.nama, style = MaterialTheme.typography.titleMedium)
            Text(text = "Stok: ${bahan.jumlah}", color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = jumlahText,
                onValueChange = { jumlahText = it },
                label = { Text("Jumlah dipakai") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val jumlahDipakai = jumlahText.toBigDecimalOrNull() ?: BigDecimal.ZERO
                    if (jumlahDipakai > BigDecimal.ZERO) {
                        onTambah(jumlahDipakai)
                        jumlahText = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Tambah ke Produk", color = Color.White)
            }
        }
    }
}
