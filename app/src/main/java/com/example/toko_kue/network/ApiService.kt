package com.example.toko_kue.network

import com.example.toko_kue.data.model.Bahan
import com.example.toko_kue.data.model.Produk
import retrofit2.http.*

interface ApiService {

    // ===== BAHAN =====
    @GET("bahanBaku")
    suspend fun getAllBahan(): List<Bahan>

    @POST("bahanBaku/tambah-stok")
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

    @POST("produk")
    suspend fun addProduk(@Body produk: Produk): Produk
}
