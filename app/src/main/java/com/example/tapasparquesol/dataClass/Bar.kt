package com.example.tapasparquesol.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Bar(
    val id: Int? = null,
    val nombreBar: String,
    var direccion: String,
    var valoracion: Float,
    var latitudLongitud: String,
    var web:String
):Parcelable
