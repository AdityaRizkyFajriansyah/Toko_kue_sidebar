package com.example.toko_kue.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.toko_kue.viewmodel.BahanViewModel
import com.example.toko_kue.viewmodel.ProdukViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputProdukScreen(
    bahanViewModel: BahanViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    produkViewModel: ProdukViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit,
    onSelesai: () -> Unit
){
    var namaProduk by remember { mutableStateOf("") }
    var hargaJual by remember { mutableStateOf("") }
    var jumlahJadi by remember { mutableStateOf("") }
    var namaBahan by remember { mutableStateOf("") }
    var jumlahBahan by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input produk ambil bahan") },
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
        ){
            
        }
    }
}