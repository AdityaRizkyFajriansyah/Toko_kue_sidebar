package com.example.toko_kue.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.toko_kue.data.model.Bahan

class BahanViewModel: ViewModel(){
    var bahanList = mutableStateListOf<Bahan>()
        private set

    fun tambahBahan(bahan: Bahan){
        bahanList.add(bahan)
    }

    fun kurangiBahan(nama: String, jumlahPakai: Double){
        val index = bahanList.indexOfFirst { it.nama == nama }
        if(index != -1){
            val bahan = bahanList[index]
            bahanList[index] = bahan.copy(stok = bahan.stok - jumlahPakai)
        }
    }
}