package com.example.toko_kue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.toko_kue.ui.theme.Toko_KueTheme
import com.example.toko_kue.screen.HomeScreen
import com.example.toko_kue.screen.DaftarBahanScreen
import com.example.toko_kue.screen.InputBahanProdukScreen
import com.example.toko_kue.viewmodel.BahanViewModel
import com.example.toko_kue.viewmodel.produkViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Toko_KueTheme {
                val navController = rememberNavController()
                val bahanViewModel: BahanViewModel = viewModel()
                val produkViewModel: produkViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ){
                    composable("home"){
                        HomeScreen(
                            navController = navController,
                            bahanViewModel = bahanViewModel,
                            produkViewModel = produkViewModel,
                            onNavigateToDaftarBahan = {
                                navController.navigate("daftarBahan")
                            },
                            onNavigateToInputProdukScreen = {
                                navController.navigate("inputProduk")
                            }
                        )
                    }

                    composable("daftarBahan"){
                        DaftarBahanScreen(
                            bahanViewModel = bahanViewModel,
                            onBack = {navController.popBackStack()}
                        )
                    }

                    composable("inputProduk"){
                        InputBahanProdukScreen(
                            produkId = "",
                            bahanViewModel = bahanViewModel,
                            navController = navController,
                            produkViewModel = produkViewModel
                        )
                    }

                    composable(
                        route = "inputBahanProduk/{produkId}",
                        arguments = listOf(navArgument("produkId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val produkId = backStackEntry.arguments?.getString("produkId")
                        produkId?.let {
                            InputBahanProdukScreen(
                                produkId = it,
                                bahanViewModel = bahanViewModel,
                                produkViewModel = produkViewModel,
                                navController = navController
                            )
                        }
                    }

                }
            }
        }
    }
}
