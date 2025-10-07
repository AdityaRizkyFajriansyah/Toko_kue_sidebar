package com.example.toko_kue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.toko_kue.ui.theme.Toko_KueTheme
import com.example.toko_kue.screen.HomeScreen
import com.example.toko_kue.screen.DaftarBahanScreen
import com.example.toko_kue.screen.InputProdukScreen
import com.example.toko_kue.viewmodel.BahanViewModel
import com.example.toko_kue.viewmodel.ProdukViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Toko_KueTheme {
                val navController = rememberNavController()
                val bahanViewModel: BahanViewModel = viewModel()
                val produkViewModel: ProdukViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ){
                    composable("home"){
                        HomeScreen(
                            navController = navController,
                            onNavigateToDaftarBahan = {
                                navController.navigate("daftarBahan")
                            }
                        )
                    }

                    composable("daftarBahan"){
                        DaftarBahanScreen(
                            onBack = {navController.popBackStack()}
                        )
                    }

                    composable("inputProduk"){
                        InputProdukScreen(
                            navController = navController,
                            bahanViewModel = bahanViewModel,
                            produkViewModel = produkViewModel,
                            onBack = { navController.popBackStack()},
                            onSelesai = { navController.navigate("home")}
                        )
                    }
                }
            }
        }
    }
}
