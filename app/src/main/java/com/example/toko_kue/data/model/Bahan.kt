package com.example.toko_kue.data.model

import com.google.gson.annotations.SerializedName

data class Bahan(
    val id: String? = null,
    val nama: String,

    @SerializedName("jumlah")
    val jumlah: Double,

    val harga: Double,

    @SerializedName("sisaBahan")
    val sisaBahan: Double? = null,

    @SerializedName("hargaDetail")
    val hargaDetail: Double? = null
)