package com.example.toko_kue.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Bahan(
    val id: String? = null,
    val nama: String,

    @SerializedName("jumlah")
    val jumlah: BigDecimal,

    val harga: BigDecimal,

    @SerializedName("sisaBahan")
    val sisaBahan: Double? = null,

    @SerializedName("hargaDetail")
    val hargaDetail: Double? = null
)

data class UpdateBahanRequest(
    val nama: String,
    val jumlah: Double,
    val harga: Double
)

data class TambahStokRequest(
    val jumlah: Double,
    val harga: Double
)