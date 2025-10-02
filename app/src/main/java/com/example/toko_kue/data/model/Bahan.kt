package com.example.toko_kue.data.model

data class Bahan(
    val id: Int,
    val nama: String,
    val stok: Double, // menggunakan gram, ml
    val harga: Double // harga satuan misalnya per kg
)