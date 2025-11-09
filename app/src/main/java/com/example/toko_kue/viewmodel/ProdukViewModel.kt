package com.example.toko_kue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toko_kue.data.model.Produk
import com.example.toko_kue.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import coil.compose.ImagePainter
import com.example.toko_kue.data.model.BahanPakaiRequest
import com.example.toko_kue.network.ApiService

class produkViewModel : ViewModel() {

    // Flow untuk menampung data produk dari backend
    private val _produkList = MutableStateFlow<List<Produk>>(emptyList())
    val produkList = _produkList.asStateFlow()

    // Instance Retrofit API
    private val api = RetrofitClient.api

    /**
     * Fungsi untuk ambil data produk dari backend
     */
    fun fetchProduk() {
        viewModelScope.launch {
            Log.d("ProdukViewModel", "🔹 Memulai fetch produk...")
            try {
                val data = api.getAllProduk() // GET dari endpoint /produk
                _produkList.value = data
                Log.d("ProdukViewModel", "✅ Berhasil ambil ${data.size} produk dari server")
            } catch (e: Exception) {
                Log.e("ProdukViewModel", "❌ Gagal fetch produk: ${e.message}", e)
            }
        }
    }

    /**
     * Tambah produk baru (POST ke backend)
     */
    fun tambahProduk(produk: Produk, onSuccess: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response: Produk = RetrofitClient.api.addProduk(produk)
                val newId = response.id ?: ""      // ambil id produk dari response
                fetchProduk()
                onSuccess(newId)                   // kirim id ke callback
            } catch (e: Exception) {
                Log.e("ProdukViewModel", "Error tambah produk: ${e.message}")
            }
        }
    }

    /**
     * Hapus produk berdasarkan ID
     */
    fun hapusProduk(id: String) {
        viewModelScope.launch {
            try {
                api.deleteProduk(id) // endpoint DELETE /produk/{id}
                fetchProduk()
                Log.d("ProdukViewModel", "✅ Produk dengan ID $id berhasil dihapus")
            } catch (e: Exception) {
                Log.e("ProdukViewModel", "❌ Error hapus produk: ${e.message}", e)
            }
        }
    }

    fun updateProduk(produk: Produk) {
        viewModelScope.launch {
            try {
                val updatedProduk = produk.copy(
                    totalHargaProduk = produk.harga.multiply(produk.jumlah)
                )
                val response = api.updateProduk(updatedProduk.id!!, updatedProduk)
                if (response.isSuccessful) {
                    Log.d("ProdukViewModel", "✅ Produk berhasil diedit: ${response.body()}")
                    fetchProduk()
                } else {
                    Log.e("ProdukViewModel", "❌ Gagal edit produk: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ProdukViewModel", "⚠️ Error updateProduk: ${e.message}", e)
            }
        }
    }

    fun tambahBahanKeProduk(produkId: String, request: BahanPakaiRequest) {
        viewModelScope.launch {
            try {
                RetrofitClient.api.addBahanPakai(
                    produkId = produkId,
                    request = request
                )
                Log.d("ProdukViewModel", "✅ Bahan ${request.bahanBakuId} ditambahkan ke produk $produkId")
            } catch (e: Exception) {
                Log.e("ProdukViewModel", "❌ Gagal tambah bahan: ${e.message}")
                throw e
            }
        }
    }

}
