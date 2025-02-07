package com.example.tapasparquesol

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class FragmentDetalles: Fragment(R.layout.fragment_info_bar), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private var bar: Bar? = null

    override fun onMapReady(googleMap: GoogleMap) {
        arguments?.let { bundle ->
            bar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("restaurant", Bar::class.java)
            } else {
                bundle.getParcelable("restaurant") as? Bar
            }

        }
        mMap = googleMap

        val latlng = LatLng(bar?.latitudLongitud!!.split(",")[1].toDouble(),bar?.latitudLongitud!!.split(",")[1].toDouble())
        mMap?.addMarker(
            MarkerOptions()
                .position(latlng)
                .title(bar?.nombreBar)
        )
        mMap!!.setOnMarkerClickListener { marker ->
            if (marker.title?.contains(bar!!.nombreBar) == true) {
                val url = "http://iesjulianmarias.centros.educa.jcyl.es/sitio/"
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(i)
                return@setOnMarkerClickListener true
            }
            false
        }


    }


}