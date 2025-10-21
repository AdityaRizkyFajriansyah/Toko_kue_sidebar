//package com.example.toko_kue.viewmodel
//
//import androidx.lifecycle.ViewModel
//import com.example.toko_kue.data.model.Produk
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//import kotlin.io.path.fileVisitor
//
//class LaporanViewModel : viewModel(){
//    fun getLaporanHarian(produkList: List<Produk>, tanggal: String): Pair<Double, Double>{
//        val filtered = produkList.filter {  it.tanggal == tanggal }
//        val omset =  filtered.sumOf { it.hargaJual * it.jumlahJadi }
//        val modal = filtered.sumOf { it.totalModal }
//        return Pair(omset, omset - modal)
//    }
//
//    fun getLaporanBulanan(produkList: List<Produk>, bulanTahun: String): Pair<Double, Double>{
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        val filtered = produkList.filter {
//            val date = (LocalDate.parse(it.tanggal, formatter))
//            val bulan = "%02d".format(date.monthValue)
//            val tahun = date.year.toString()
//            "$tahun-$bulan" == bulanTahun
//        }
//        val omset = filtered.sumOf { it.hargaJual * it.jumlahJadi }
//        val modal = filtered.sumOf { it.totalModal }
//        return Pair(omset, omset-modal)
//    }
//
//    fun getBulanTahunDariProdukp(produkList: List<Produk>): List<String>{
//        val formatter = java.time.format.DateTimeFormatter,ofPattern("yyyy-MM-dd")
//    }
//}