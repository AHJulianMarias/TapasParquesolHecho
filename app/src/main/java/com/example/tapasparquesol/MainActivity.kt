package com.example.tapasparquesol

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tapasparquesol.dataClass.Bar
import com.example.tapasparquesol.helper.dbHelper


class MainActivity : AppCompatActivity() {
    private lateinit var dbHandler: dbHelper
    private val tagLog = "barDebug"
    private val PREF_NAME = "ULTIMO_BAR"
    private val KEY_RESPONSE = "Bar"
    private lateinit var buttonBBDD:Button
    private lateinit var bottonAcercaDe: ImageButton
    private lateinit var titulo: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        var bar: Bar? = getBarDataClass(this)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        bottonAcercaDe = toolbar.findViewById(R.id.imageButtonAcercaDe)
        titulo = toolbar.findViewById(R.id.tvToolBar)
        dbHandler = dbHelper(this)
        if (bar == null) {
            Log.d("Main", "No hay ningun bar en sharedPreferences")
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FragmentList())
                .commit()
        }else{
            Log.d("Main", "Bar encontrado en SharePreferences")
            val bundle = Bundle().apply {
                putParcelable("Bar", bar)
            }
            val destinationFragment = FragmentDetalles().apply {
                arguments = bundle
            }
            this.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit()

        }

        titulo.setOnClickListener{
            this.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FragmentList())
                .addToBackStack(null)
                .commit()
        }

        bottonAcercaDe.setOnClickListener{
            val dialogAcercaDe = Dialog(this)
            dialogAcercaDe.setContentView(R.layout.dialog_nombre)
            dialogAcercaDe.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialogAcercaDe.setCancelable(false)
            dialogAcercaDe.show()
            val buttonSalir = dialogAcercaDe.findViewById<Button>(R.id.buttonSalirDialog)
            buttonSalir.setOnClickListener{
                dialogAcercaDe.dismiss()
            }

        }


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



}