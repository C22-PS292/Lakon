package com.bangkit2022.capstone.lakon.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wayang(
    val nama: String,
    val deskripsi: String,
    val referensi: String,
    val avatar: String
): Parcelable{
    constructor():this("", "","","")
}
