//package com.example.toko_kue.screen
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.background
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.toko_kue.viewmodel.LaporanViewModel
//import com.example.toko_kue.viewmodel.ProdukViewModel
//import java.time.LocalDate
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LaporanHarianScreen(
//    navController: NavController,
//    produkViewModel: LaporanViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
//    laporanViewModel: LaporanViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
//){
//    var selectedDate by remember { mutableStateOf(LocalDate.now.toString())}
//
//    val(omset, laba) = laporanViewModel.getLaporanHarian(produkViewModel.produkList, selectedDate)
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("laporan Omset Harian") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() } ){
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
//                    }
//                }
//            )
//        }
//    ) {
//        padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .fillMaxSize()
//                .background(Color(0xFFE0F2F1))
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally(16.dp)
//        ) {
//            OutlinedTextField(
//                value = selectedDate,
//                onValueChange = { selectedDate = it},
//                label = { Text("Tanggal (format: yyyy-MM-dd") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Button(
//                onClick = { /* Refresh Otomatis*/},
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Tampilkan Laporan")
//            }
//            Text("Omset hari ini: Rp.${"%.0f".format(omset)}", style = MaterialTheme.typography.titleMedium)
//            Text("Laba Bersih : Rp.${"%,0f".format(omset)}", style = MaterialTheme.typography.titleMedium)
//        }
//    }
//}