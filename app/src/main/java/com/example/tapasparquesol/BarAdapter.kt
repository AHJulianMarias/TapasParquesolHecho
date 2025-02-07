package com.example.tapasparquesol

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson

class BarAdapter(
    context: Context,
    private val bares: List<Bar>
) : ArrayAdapter<Bar>(context, 0, bares) {
    private val PREF_NAME = "ULTIMO_BAR"
    private val KEY_RESPONSE = "Bar"
    private val tagLog = "barDebug"
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        // Inflar el layout si no existe vista reciclada
        if (view == null) {
            view = LayoutInflater.from(context)
                .inflate(R.layout.item_bar, parent, false)
        }

        // Obtener el objeto restaurant actual
        val bar = bares[position]

        // Referencias a los views
        val tvName = view?.findViewById<TextView>(R.id.tvNombreBar)
        val ratingBar = view?.findViewById<RatingBar>(R.id.ratingBarLista)
        // Asignar valores
        tvName?.text = bar.nombreBar
        ratingBar?.rating = bar.valoracion
        view?.setOnClickListener {
            // Implementar la lógica de navegación
            val bundle = Bundle().apply {
                putParcelable("bar", bar)
            }
            saveLastBar(context,bar)
            val destinationFragment = FragmentDetalles().apply {
                arguments = bundle
            }
            (context as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit()
        }
        return view!!
    }

    override fun getCount(): Int {
        return bares.size
    }

    override fun getItem(position: Int): Bar? {
        return bares[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    fun saveLastBar(context: Context, ultimoBar: Bar?){
        val gson = Gson()
        val json = gson.toJson(ultimoBar)
        val preferences = getPreferences(context).edit()
        Log.d(tagLog,"Guardado $json")
        preferences.putString(KEY_RESPONSE,json)
        preferences.apply()
    }
}
