package com.example.tapasparquesol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.tapasparquesol.dbHelper

class FragmentList: Fragment() {

    private lateinit var dbHandler: dbHelper
    private lateinit var listView: ListView
    private lateinit var adapter: BarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lista_bares,container,false)
        listView = view.findViewById(R.id.ListView)
        dbHandler = dbHelper(requireContext())
        return view
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        adapter = BarAdapter(requireContext(),dbHandler.getAllBares())
//        listView.adapter = adapter
//
//    }


}