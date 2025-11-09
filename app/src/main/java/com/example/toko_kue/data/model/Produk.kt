package com.example.toko_kue.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Produk(
    val id: String? = null,
    val nama: String,
    val jumlah: BigDecimal,
    val harga: BigDecimal,
    @SerializedName("totalHargaProduk")  // <── ubah ke nama field backend
    val totalHargaProduk: BigDecimal,
    val modal: BigDecimal? = BigDecimal.ZERO,
    @SerializedName("bahanBakuPakai")
    val bahanBakuPakai: List<BahanPakaiResponse>? = null
)


data class BahanPakaiRequest(
    val bahanBakuId: String,
    @SerializedName("bahanBakuPakai")
    val bahanBakuPakai: BigDecimal
)

data class BahanPakaiResponse(
    val bahanBakuId: String?,
    val bahanBakuNama: String?,
    val bahanBakuPakai: BigDecimal?
)