package com.example.toko_kue.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Produk(
    val id: String? = null,
    val nama: String,

    @SerializedName("jumlah")
    val jumlah: BigDecimal = BigDecimal.ZERO,

    @SerializedName("harga")
    val harga: BigDecimal = BigDecimal.ZERO,

    @SerializedName("totalHargaProduk")
    val totalHargaProduk: BigDecimal = BigDecimal.ZERO,

    @SerializedName("bahanBakuPakai")
    val bahanBakuPakai: List<BahanPakai> = emptyList()
)

data class BahanPakai(
    val id: String? = null,
    val produkId: String? = null,
    val bahanBakuId: String? = null,
    val bahanBakuNama: String? = null,

    @SerializedName("bahanBakuPakai")
    val jumlahDipakai: BigDecimal = BigDecimal.ZERO
)
