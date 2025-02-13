package com.example.tapasparquesol.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.tapasparquesol.dataClass.Bar

class dbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "BarDatabase"
        private const val DATABASE_VERSION = 1
        private const val TABLE_BARES = "Bar"
        private const val KEY_ID = "Id"
        private const val KEY_NOMBRE = "NombreBar"
        private const val KEY_DIRECCION = "Direccion"
        private const val KEY_VALORACION = "Valoracion"
        private const val KEY_LatitudLongitud = "LatitudLongitud"
        private const val KEY_WEB = "Web"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createBarsTable = ("CREATE TABLE " + TABLE_BARES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NOMBRE + " TEXT,"
                + KEY_DIRECCION + " TEXT,"
                + KEY_VALORACION + " REAL,"
                + KEY_LatitudLongitud + " TEXT,"
                + KEY_WEB + " TEXT " +
                ")")

        db?.execSQL(createBarsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BARES")
        onCreate(db)
    }

    fun getAllBares(): ArrayList<Bar> {
        val BaresList = ArrayList<Bar>()
        val selectQuery = "SELECT * FROM $TABLE_BARES"

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var nombre: String
        var direccion: String
        var valoracion:String
        var latlong: String
        var web:String

        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(KEY_ID)
                val nombreIndex = cursor.getColumnIndex(KEY_NOMBRE)
                val direccionIndex = cursor.getColumnIndex(KEY_DIRECCION)
                val valoracionIndex =cursor.getColumnIndex(KEY_VALORACION)
                val latlongIndex =cursor.getColumnIndex(KEY_LatitudLongitud)
                val webIndex =cursor.getColumnIndex(KEY_WEB)

                if (idIndex != -1 && nombreIndex != -1 && direccionIndex != -1 && valoracionIndex != -1 && latlongIndex != -1 && webIndex != -1) {
                    id = cursor.getInt(idIndex)
                    nombre = cursor.getString(nombreIndex)
                    direccion = cursor.getString(direccionIndex)
                    valoracion = cursor.getString(valoracionIndex)
                    latlong= cursor.getString(latlongIndex)
                    web= cursor.getString(webIndex)

                    val bar = Bar(id = id,
                        nombreBar = nombre,
                        direccion = direccion,
                        valoracion = valoracion.toFloat(),
                        latitudLongitud = latlong,
                        web = web)
                    BaresList.add(bar)
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        return BaresList
    }

    fun updateBar(Bar: Bar): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NOMBRE, Bar.nombreBar)
        contentValues.put(KEY_DIRECCION, Bar.direccion)
        contentValues.put(KEY_VALORACION, Bar.valoracion)
        contentValues.put(KEY_LatitudLongitud, Bar.latitudLongitud)
        contentValues.put(KEY_WEB, Bar.web)

        return db.update(TABLE_BARES, contentValues, "$KEY_ID = ?", arrayOf(Bar.id.toString()))
    }

    fun deleteBar(Bar: Bar): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_BARES, "$KEY_ID = ?", arrayOf(Bar.id.toString()))
        db.close()
        return success
    }

    fun addBar(Bar: Bar): Long {
        try {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(KEY_NOMBRE, Bar.nombreBar)
            contentValues.put(KEY_DIRECCION, Bar.direccion)
            contentValues.put(KEY_VALORACION, Bar.valoracion)
            contentValues.put(KEY_LatitudLongitud, Bar.latitudLongitud)
            contentValues.put(KEY_WEB, Bar.web)
            val success = db.insert(TABLE_BARES, null, contentValues)
            db.close()
            return success
        } catch (e: Exception) {
            Log.e("Error", "Error al agregar Bar", e)
            return -1
        }
    }
}


