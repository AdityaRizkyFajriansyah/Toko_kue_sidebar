package com.example.toko_kue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.toko_kue.data.model.Bahan
import com.example.toko_kue.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.example.toko_kue.data.model.TambahStokRequest
import com.example.toko_kue.data.model.UpdateBahanRequest
import java.math.BigDecimal


class BahanViewModel : ViewModel() {

    private val _bahanList = MutableStateFlow<List<Bahan>>(emptyList())
    val bahanList = _bahanList.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()
    private val api = RetrofitClient.api
    fun fetchBahan() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val data = api.getAllBahan()
                _bahanList.value = data
            }catch(e: Exception){
                Log.e("BahanViewModel", "Error fetch bahan: ${e.message}")
            }
            finally {
                _isRefreshing.value = false
            }
        }
    }


    fun tambahBahan(nama: String, jumlah: BigDecimal, harga: BigDecimal) {
        viewModelScope.launch {
            try {
                val bahan = Bahan(
                    id = null,
                    nama = nama,
                    jumlah = jumlah,
                    harga = harga
                )
                RetrofitClient.api.addBahan(bahan)
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

//    fun kurangiBahan(namaBahan: String, jumlahKurang: Double){
//        viewModelScope.launch {
//            try{
//                val bahan = _bahanList.value.find { it.nama.equals(namaBahan, ignoreCase = true) }
//                if(bahan != null){
//                    val newJumlah = bahan.jumlah.toDouble() - jumlahKurang
//                    val updated = bahan.copy(jumlah = BigDecimal.valueOf(newJumlah))
//                    updateBahan(updated)
//                }
//            } catch (e: Exception){
//                Log.e("BahnaViewModel", "Error kurangi bahan: ${e.message}")
//            }
//        }
//    }

    fun tambahStok(id: String, jumlahTambah: Double, hargaBaru: Double) {
        viewModelScope.launch {
            try {
                val body = TambahStokRequest(
                    jumlah = jumlahTambah,
                    harga = hargaBaru
                )

                api.tambahStok(id, body)
                fetchBahan()

            } catch (e: Exception) {
                Log.e("BahanViewModel", "Error tambah stok: ${e.message}")
            }
        }
    }

    fun editBahan(bahan: Bahan) {
        viewModelScope.launch {
            try {
                val body = UpdateBahanRequest(
                    nama = bahan.nama,
                    jumlah = bahan.jumlah.toDouble(),
                    harga = bahan.harga.toDouble()
                )

                api.editBahan(bahan.id!!, body)
                fetchBahan()

            } catch (e: Exception) {
                Log.e("BahanViewModel", "Error edit bahan: ${e.message}")
            }
        }
    }




}
