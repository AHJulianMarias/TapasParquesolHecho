package com.example.tapasparquesol

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tapasparquesol.dataClass.Bar
import com.example.tapasparquesol.helper.dbHelper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentDetalles: Fragment(R.layout.fragment_info_bar), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private var bar: Bar? = null
    private lateinit var nombreBar: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var direccionBar: TextView
    private lateinit var webBar: TextView
    private lateinit var botonEditar: Button
    private lateinit var botonEliminar:Button
    private lateinit var botonPuntuar:Button
    private lateinit var dbHandler: dbHelper
    private var logitudlatitud: String? = null
    private val PREF_NAME = "ULTIMO_BAR"
    private val KEY_RESPONSE = "Bar"



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_bar,container,false)
        arguments?.let { bundle ->
            bar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("bar", Bar::class.java)
            } else {
                bundle.getParcelable("bar") as? Bar
            }

        }
        if (bar == null) {
            bar = getBarDataClass(requireContext())
        }

        dbHandler = dbHelper(requireContext())
        nombreBar = view.findViewById(R.id.tv_nombreBarDetalles)
        ratingBar = view.findViewById(R.id.rating_bar_detalles)
        webBar = view.findViewById(R.id.tv_webBarDetalles)
        direccionBar = view.findViewById(R.id.tv_direccionBarDetalles)
        botonEditar = view.findViewById(R.id.buttonEditarDetalles)
        botonEliminar = view.findViewById(R.id.buttonEliminarDetalles)
        botonPuntuar = view.findViewById(R.id.buttonPuntuarDetalles)

        direccionBar.text = bar?.direccion
        nombreBar.text = bar?.nombreBar
        webBar.text = bar?.web
        ratingBar.rating = bar!!.valoracion

        webBar.setOnClickListener{
            try {
                val url = "http://iesjulianmarias.centros.educa.jcyl.es/sitio/"
                if (!url.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(Intent.createChooser(intent, "Abrir enlace con..."))
                } else {
                    Toast.makeText(requireContext(), "URL no válida", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al abrir la URL", Toast.LENGTH_SHORT).show()
            }

        }


        botonEditar.setOnClickListener{
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_anadir)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.setCancelable(false)
            dialog.show()
            val tvTextoAnadir = dialog.findViewById<TextView>(R.id.tvAnadir)
            tvTextoAnadir.text = "Modificar bar"
            val buttonSalir = dialog.findViewById<Button>(R.id.buttonCancelarAnadir)
            buttonSalir.setOnClickListener{
                dialog.dismiss()
            }
            val etNombre = dialog.findViewById<EditText>(R.id.etNombreBar)
            etNombre.visibility =View.INVISIBLE
            val etDireccion = dialog.findViewById<EditText>(R.id.etDireccion)
            val etLongLat = dialog.findViewById<EditText>(R.id.etlongitudlatitud)
            val etweb = dialog.findViewById<EditText>(R.id.etweb)
            val buttonConfirmar = dialog.findViewById<Button>(R.id.buttonConfirmarAnadir)
            buttonConfirmar.setOnClickListener{

                if (
                    etDireccion.text.toString().isEmpty() ||
                    etLongLat.text.toString().isEmpty() ||
                    etweb.text.toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Por favor, rellena todos los campos", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                bar?.apply {
                    direccion = etDireccion.text.toString()
                    latitudLongitud = etLongLat.text.toString()
                    web = etweb.text.toString()
                }
                updateBar(bar!!)

                dialog.dismiss()


            }

        }
        botonEliminar.setOnClickListener{
            deleteBar(bar!!)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FragmentList())
                .addToBackStack(null)
                .commit()
            val preferences = getPreferences(requireContext()).edit()
            preferences.clear()
            preferences.apply()
        }
        botonPuntuar.setOnClickListener{
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_puntuar)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.setCancelable(false)
            dialog.show()
            val buttonSalir = dialog.findViewById<Button>(R.id.buttonCancelarPuntuar)
            val buttonConfirmar = dialog.findViewById<Button>(R.id.buttonConfirmarPuntuar)
            val rating = dialog.findViewById<RatingBar>(R.id.ratingBarPuntuar)

            buttonSalir.setOnClickListener{
                dialog.dismiss()
            }
            buttonConfirmar.setOnClickListener{
                bar!!.valoracion = rating.rating
                updateBar(bar!!)
                dialog.dismiss()
            }

        }

        return view
    }


    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        try{
            val latlng = LatLng(bar?.latitudLongitud!!.split(",")[0].toDouble(),bar?.latitudLongitud!!.split(",")[1].toDouble())
            mMap?.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .title(bar?.nombreBar)
            )
            mMap!!.setOnMarkerClickListener { marker ->
                if (marker.title?.contains(bar!!.nombreBar) == true) {
                    val url = "http://iesjulianmarias.centros.educa.jcyl.es/sitio/"
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(Intent.createChooser(i, "Abrir enlace con..."))
                    return@setOnMarkerClickListener true
                }
                false
            }
        }catch (e:Exception){
            Toast.makeText(requireContext(),"latitud y longitud en el formato incorrecto, modifica los valores.",Toast.LENGTH_LONG).show()
        }



    }
    fun getBarDataClass(context: Context): Bar?{
        val gson = Gson()
        //null por defecto, si no hay nada devuelve null
        val json = getPreferences(context).getString(KEY_RESPONSE,null)
        if(json.isNullOrEmpty()){
            return null
        }else{
            return gson.fromJson(json, Bar::class.java)
        }
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }



    private fun updateBar(bar: Bar){
        //Sin corrutinas quitas lo de alrededor y ya
        CoroutineScope(Dispatchers.Main).launch {
            if (bar == null) {
                Toast.makeText(requireContext(), "El bar no existe", Toast.LENGTH_LONG).show()
            }else{
                bar.apply {
                    if (direccion != this.direccion) this.direccion = direccion
                    if (valoracion != this.valoracion) this.valoracion = valoracion
                    if (logitudlatitud != null) {
                        if (latitudLongitud != logitudlatitud) this.latitudLongitud = logitudlatitud as String
                    }
                    if (web != this.web) this.web = web
                }
                val status = dbHandler.updateBar(bar)
                if (status > -1) {
                    Toast.makeText(requireContext(), "Bar modificado correctamente", Toast.LENGTH_LONG).show()
                    nombreBar.text = bar.nombreBar
                    ratingBar.rating = bar.valoracion
                    webBar.text = bar.web
                    direccionBar.text = bar.direccion

                }
            }
        }


    }
    private fun deleteBar(bar: Bar){
        //Sin corrutinas quitas lo de alrededor y ya
        CoroutineScope(Dispatchers.Main).launch {
            val status = dbHandler.deleteBar(bar)
            if (status > -1) {
                Toast.makeText(requireContext(), "Bar eliminado", Toast.LENGTH_LONG).show()

            }
        }
    }
}