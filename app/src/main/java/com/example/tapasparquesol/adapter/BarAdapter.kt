package com.example.tapasparquesol.adapter

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.tapasparquesol.FragmentDetalles
import com.example.tapasparquesol.R
import com.example.tapasparquesol.dataClass.Bar
import com.google.gson.Gson

class BarAdapter(
    context: Context,
    private var bares: List<Bar>
) : ArrayAdapter<Bar>(context, 0, bares) {
    private val PREF_NAME = "ULTIMO_BAR"
    private val KEY_RESPONSE = "Bar"
    private val tagLog = "barDebug"
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context)
                .inflate(R.layout.item_bar, parent, false)
        }

        val bar = bares[position]

        val tvName = view?.findViewById<TextView>(R.id.tvNombreBar)
        val webBar = view?.findViewById<TextView>(R.id.tvWebBar)
        tvName?.text = bar.nombreBar
        webBar?.text = bar.web
        view?.setOnClickListener {
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
    fun updateData(newBares: List<Bar>) {
        bares = newBares
        notifyDataSetChanged()
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
