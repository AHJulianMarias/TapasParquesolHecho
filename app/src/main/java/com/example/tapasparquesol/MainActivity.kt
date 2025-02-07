package com.example.tapasparquesol

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.gson.Gson
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {
    private lateinit var dbHandler: dbHelper
    private val tagLog = "barDebug"
    private val PREF_NAME = "ULTIMO_BAR"
    private val KEY_RESPONSE = "Bar"
    private lateinit var buttonBBDD:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        var bar: Bar? = getBarDataClass(this)
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

        buttonBBDD = findViewById(R.id.buttonbbdd)

        buttonBBDD.setOnClickListener{
            val intent = Intent(this, bbddActivity::class.java)
            startActivity(intent)


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

    fun getBarDataClass(context: Context):Bar?{
        val gson = Gson()
        //null por defecto, si no hay nada devuelve null
        val json = getPreferences(context).getString(KEY_RESPONSE,null)
        if(json.isNullOrEmpty()){
            return null
        }else{
            return gson.fromJson(json,Bar::class.java)
        }
    }



}