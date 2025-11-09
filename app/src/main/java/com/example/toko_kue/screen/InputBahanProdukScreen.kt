@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.toko_kue.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.toko_kue.data.model.Bahan
import com.example.toko_kue.data.model.BahanPakaiRequest
import com.example.toko_kue.viewmodel.BahanViewModel
import com.example.toko_kue.viewmodel.produkViewModel
import java.math.BigDecimal
import kotlinx.coroutines.launch

@Composable
fun inputBahanProdukScreen(
    produkId: String,
    navController: NavController,
    bahanViewModel: BahanViewModel,
    produkViewModel: produkViewModel
) {
    val bahanList by bahanViewModel.bahanList.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // ambil daftar bahan dari server
    LaunchedEffect(Unit) {
        bahanViewModel.fetchBahan()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input Bahan untuk Produk") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF6F6F6))
        ) {
            if (bahanList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Belum ada data bahan.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(bahanList) { bahan ->
                        BahanItemCard(
                            bahan = bahan,
                            produkId = produkId,
                            produkViewModel = produkViewModel,
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    "${bahan.nama} berhasil ditambahkan ke produk!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onError = { message ->
                                Toast.makeText(
                                    context,
                                    "Gagal menambah bahan: $message",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BahanItemCard(
    bahan: Bahan,
    produkId: String,
    produkViewModel: produkViewModel,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    var jumlahText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = bahan.nama ?: "Tanpa nama",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Stok: ${bahan.jumlah}")

            OutlinedTextField(
                value = jumlahText,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) jumlahText = input
                },
                label = { Text("Jumlah dipakai") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val jumlahDipakai = jumlahText.toBigDecimalOrNull() ?: BigDecimal.ZERO
                    if (jumlahDipakai > BigDecimal.ZERO) {
                        isLoading = true
                        scope.launch {
                            try {
                                val bahanPakai = BahanPakaiRequest(
                                    bahanBakuId = bahan.id ?: "",
                                    bahanBakuPakai = jumlahDipakai
                                )
                                produkViewModel.tambahBahanKeProduk(produkId, bahanPakai)
                                jumlahText = ""
                                onSuccess()
                            } catch (e: Exception) {
                                onError(e.message ?: "Terjadi kesalahan.")
                            } finally {
                                isLoading = false
                            }
                        }
                    } else {
                        onError("Jumlah tidak boleh kosong atau 0")
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.CenterVertically),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Tambah ke Produk", color = Color.White)
                }
            }
        }
    }
}
