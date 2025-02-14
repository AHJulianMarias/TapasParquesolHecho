package com.example.tapasparquesol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tapasparquesol.adapter.spinnerAdapter
import com.example.tapasparquesol.helper.dbHelper


class FragmentVista2: Fragment(),AdapterView.OnItemSelectedListener {
    private lateinit var dbHandler: dbHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_spinner, container, false)
        val spinnerBares = view.findViewById<Spinner>(R.id.spinner)
        dbHandler = dbHelper(requireContext())
        val adaptadorPerso = spinnerAdapter(requireContext(),R.layout.item_spinner,dbHandler.getAllBares())

        spinnerBares.adapter = adaptadorPerso
        spinnerBares.onItemSelectedListener = this
        return view
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val c = view?.findViewById<TextView>(R.id.descripcionNombredelBar)
        val seleccion = requireView().findViewById<TextView>(R.id.tvItemSelected)
        seleccion?.text = c?.text
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        val seleccion = requireView().findViewById<TextView>(R.id.tvItemSelected)
        seleccion?.text = "Nada seleccionado"
    }

}