package com.example.toko_kue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toko_kue.data.model.Produk
import com.example.toko_kue.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class ProdukViewModel : ViewModel() {

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
    fun tambahProduk(produk: Produk) {
        viewModelScope.launch {
            try {
                api.addProduk(produk) // endpoint POST /produk
                fetchProduk() // refresh data dari server
                Log.d("ProdukViewModel", "✅ Produk berhasil ditambahkan")
            } catch (e: Exception) {
                Log.e("ProdukViewModel", "❌ Error tambah produk: ${e.message}", e)
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

    fun updateProduk(produk: Produk){
        viewModelScope.launch {
            try{
                produk.id?.let { id ->
                    api.updateProduk(id, produk)
                    fetchProduk()
                    Log.d("ProdukViewModel", "Produk berhasil di update: ${produk.nama}")
                } ?: Log.e("ProdukViewModel", "ID produk null, tidak bisa update")
            } catch (e: Exception){
                Log.e("ProdukViewModel", "Gagal update produk: ${e.message}", e)
        }
        }
    }
}
