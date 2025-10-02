package com.example.toko_kue.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.toko_kue.data.model.Produk

class ProdukViewModel : ViewModel() {
    var produkList = mutableListOf<Produk>()
        private set

    fun tambahProduk(produk: Produk){
        produkList.add(produk)
    }
}