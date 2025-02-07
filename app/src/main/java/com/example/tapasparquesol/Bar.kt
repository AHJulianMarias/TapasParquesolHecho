package com.example.tapasparquesol

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Bar(
    val id: Int,
    val nombreBar: String,
    val direccion: String,
    val valoracion: Float,
    val latitudLongitud: String,
    val web:String
):Parcelable
