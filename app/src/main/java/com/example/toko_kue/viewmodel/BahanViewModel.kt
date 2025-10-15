package com.example.toko_kue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toko_kue.data.model.Bahan
import com.example.toko_kue.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class BahanViewModel : ViewModel() {

    private val _bahanList = MutableStateFlow<List<Bahan>>(emptyList())
    val bahanList = _bahanList.asStateFlow()

    private val api = RetrofitClient.api

    fun fetchBahan() {
        viewModelScope.launch {
            try {
                val data = api.getAllBahan()
                _bahanList.value = data
                Log.d("BahanViewModel", "Fetch sukses: ${data.size} item")
            } catch (e: Exception) {
                Log.e("BahanViewModel", "Error fetch bahan: ${e.message}")
            }
        }
    }

    fun tambahBahan(bahan: Bahan) {
        viewModelScope.launch {
            try {
                api.addBahan(bahan)
                fetchBahan()
            } catch (e: Exception) {
                Log.e("BahanViewModel", "Error tambah bahan: ${e.message}")
            }
        }
    }

    fun hapusBahan(id: String) {
        viewModelScope.launch {
            try {
                api.deleteBahan(id)
                fetchBahan()
            } catch (e: Exception) {
                Log.e("BahanViewModel", "Error hapus bahan: ${e.message}")
            }
        }
    }

    fun updateBahan(bahan: Bahan) {
        viewModelScope.launch {
            try {
                api.updateBahan(bahan.id!!, bahan)
                fetchBahan()
            } catch (e: Exception) {
                Log.e("BahanViewModel", "Error update bahan: ${e.message}")
            }
        }
    }

    fun kurangiBahan(namaBahan: String, jumlahDikurangi: Double) {
        viewModelScope.launch {
            try {
                val bahan = _bahanList.value.find { it.nama == namaBahan }
                if (bahan != null) {
                    val bahanBaru = bahan.copy(jumlah = bahan.jumlah - jumlahDikurangi)
                    api.updateBahan(bahan.id!!, bahanBaru)
                    fetchBahan()
                }
            } catch (e: Exception) {
                Log.e("BahanViewModel", "Error kurangi bahan: ${e.message}")
            }
        }
    }
}
