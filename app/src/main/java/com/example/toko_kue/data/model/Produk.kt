package com.example.toko_kue.data.model

data class Produk(
    val id: Int,
    val nama: String,
    val hargaJual: Double,
    val jumlahJadi: Int,
    // (nama bahan, Jumlah Dipakai
    val bahanDipakai: List<Pair<String, Double>>
)