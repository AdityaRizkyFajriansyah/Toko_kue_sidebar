//package com.example.toko_kue.screen
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.background
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.toko_kue.viewmodel.LaporanViewModel
//import com.example.toko_kue.viewmodel.ProdukViewModel
//import kotlinx.coroutines.selects.select
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LaporanBulananScreen(
//    navController: NavController,
//    produkViewModel: ProdukViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
//    laporanViewModel: LaporanViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
//) {
//    val produkList = produkViewModel.produkList
//    val daftarBulan = laporanViewModel.getBulanTahunDariProduk(produkList)
//
//    var expanded by remember { mutableStateOf(false)}
//    var selectedBahan by remember { mutableStateOf(daftarBulan.lastOrNull() ?: "")}
//
//    val (omzet, laba) = remember(selectedBahan){
//        laporanViewModel.getLaporanBulanan()
//    }
//}