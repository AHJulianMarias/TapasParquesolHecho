package com.example.tapasparquesol.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.tapasparquesol.R
import com.example.tapasparquesol.dataClass.Bar

class spinnerAdapter (context: Context, resource: Int, private val bares: List<Bar>) : ArrayAdapter<Bar>(context, resource, bares) {

    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        return crearFilaPersonalizada(position, convertView, parent)
    }
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        return crearFilaPersonalizada(position, convertView, parent)
    }

    private fun crearFilaPersonalizada(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        val layoutInflater = LayoutInflater.from(context)

        val rowView = convertView ?: layoutInflater.inflate(R.layout.item_spinner, parent, false)
        val bar = bares[position]

        rowView.findViewById<TextView>(R.id.descripcionNombredelBar).text = bar.nombreBar


        //rowView.findViewById<ImageView>(R.id.imagenCiudad).setImageResource(imagenes[position])

        return rowView
    }
}
