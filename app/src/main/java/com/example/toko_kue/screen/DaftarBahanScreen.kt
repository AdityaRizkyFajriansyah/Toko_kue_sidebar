package com.example.toko_kue.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.toko_kue.viewmodel.BahanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarBahanScreen(
    bahanViewModel: BahanViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit
) {
    val bahanList = bahanViewModel.bahanList

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
    ){
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Bahan") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch{ drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: tambah bahan */ }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Bahan")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFB2DFDB))
        ) {
            Text(
                "Daftar Bahan Baku",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp),
                style = MaterialTheme.typography.titleLarge
            )

            // ambil langsung dari viewModel
            bahanList.forEach{ bahan ->
                Text(
                    "${bahan.nama} - stok: ${bahan.stok} - Harga: Rp.${bahan.harga}",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
}
