package com.example.toko_kue.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toko_kue.data.model.Produk
import com.example.toko_kue.viewmodel.ProdukViewModel
import java.math.BigDecimal
import kotlin.collections.lastIndex
import kotlin.collections.lastOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputProdukScreen(
    navController: NavController,
    produkViewModel: ProdukViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    var nama by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input Produk Baru") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (nama.isNotEmpty() && jumlah.isNotEmpty() && harga.isNotEmpty()) {
                        val produkBaru = Produk(
                            id = null,
                            nama = nama,
                            jumlah = jumlah.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                            harga = harga.toBigDecimalOrNull() ?: BigDecimal.ZERO
                        )

                        produkViewModel.tambahProduk(produkBaru)

                        // Reset form setelah input
                        nama = ""
                        jumlah = ""
                        harga = ""
                    }
                },
                containerColor = Color(0xFF00796B)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Produk", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Tambah Produk",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )

            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama Produk") },
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
                label = { Text("Harga per pcs") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (nama.isNotEmpty() && jumlah.isNotEmpty() && harga.isNotEmpty()) {
                        val produkBaru = Produk(
                            id = null,
                            nama = nama,
                            jumlah = jumlah.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                            harga = harga.toBigDecimalOrNull() ?: BigDecimal.ZERO
                        )
                        produkViewModel.tambahProduk(produkBaru)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Simpan Produk", color = Color.White)
            }
        }
    }
}
