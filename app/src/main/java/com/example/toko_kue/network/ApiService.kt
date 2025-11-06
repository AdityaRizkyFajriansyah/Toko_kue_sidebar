package com.example.toko_kue.network

import com.example.toko_kue.data.model.Bahan
import com.example.toko_kue.data.model.BahanPakaiRequest
import com.example.toko_kue.data.model.Produk
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {

    // ===== BAHAN =====
    @GET("/bahanBaku")
    suspend fun getAllBahan(): List<Bahan>

    @POST("bahanBaku/stok-baru")
    suspend fun addBahan(@Body bahan: Bahan): Bahan

    @DELETE("bahanBaku/{id}")
    suspend fun deleteBahan(@Path("id") id: String)

    @PUT("bahanBaku/{id}")
    suspend fun updateBahan(
        @Path("id") id: String,
        @Body bahan: Bahan
    ): Bahan

    // ===== PRODUK =====
    @GET("produk")
    suspend fun getAllProduk(): List<Produk>

    @POST("produk/tambahProduk")
    suspend fun addProduk(@Body produk: Produk): Produk

    @DELETE("/produk/{id}")
    suspend fun deleteProduk(@Path("id") id: String)

    @PUT("produk/{id}")
    suspend fun updateProduk(
        @Path("id") id: String,
        @Body produk: Produk
    ): retrofit2.Response<Produk>

    // input bahan pakai untuk produk
    @POST("produk/{produkId}/bahanBakuProduk")
    suspend fun addBahanPakai(
        @Path("produkId") produkId: String,
        @Body request: BahanPakaiRequest
    )
}
