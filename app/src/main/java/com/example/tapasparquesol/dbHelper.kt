package com.example.tapasparquesol

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import com.example.tapasparquesol.Bar

// Clase DatabaseHelper que extiende SQLiteOpenHelper para manejar la base de datos de la aplicación.
class dbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Bloque companion object para definir constantes que serán usadas en toda la clase.
    // Son como los valores estáticos en Java
    companion object {
        // Nombre de la base de datos.
        private const val DATABASE_NAME = "BarDatabase"
        // Versión de la base de datos, útil para manejar actualizaciones esquemáticas.
        private const val DATABASE_VERSION = 1
        private const val TABLE_BARES = "Bar"
        private const val KEY_ID = "Id"
        private const val KEY_NOMBRE = "NombreBar"
        private const val KEY_DIRECCION = "Direccion"
        private const val KEY_VALORACION = "Valoracion"
        private const val KEY_LatitudLongitud = "LatitudLongitud"
        private const val KEY_WEB = "Web"
    }

    // Método llamado cuando la base de datos se crea por primera vez.
    override fun onCreate(db: SQLiteDatabase?) {
        // Define la sentencia SQL para crear la tabla de Bars.
        val createBarsTable = ("CREATE TABLE " + TABLE_BARES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NOMBRE + " TEXT,"
                + KEY_DIRECCION + " TEXT,"
                + KEY_VALORACION + " REAL,"
                + KEY_LatitudLongitud + " TEXT,"
                + KEY_WEB + " TEXT " +
                ")")

        // Ejecuta la sentencia SQL para crear la tabla.
        db?.execSQL(createBarsTable)
    }

    // Método llamado cuando se necesita actualizar la base de datos, por ejemplo, cuando se incrementa DATABASE_VERSION.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Elimina la tabla existente y crea una nueva.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BARES")
        onCreate(db)
    }

    // Método para obtener todos los Bars de la base de datos.
    fun getAllBares(): ArrayList<Bar> {
        // Lista para almacenar y retornar los Bars.
        val BaresList = ArrayList<Bar>()
        // Consulta SQL para seleccionar todos los Bars.

        val selectQuery = "SELECT $TABLE_BARES"

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            // Ejecuta la consulta SQL.
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            // Maneja la excepción en caso de error al ejecutar la consulta.
            db.execSQL(selectQuery)
            return ArrayList()
        }

        // Variables para almacenar los valores de las columnas.
        var id: Int
        var nombre: String
        var direccion: String
        var valoracion:String
        var latlong: String
        var web:String

        // Itera a través del cursor para leer los datos de la base de datos.
        if (cursor.moveToFirst()) {
            do {
                // Obtiene los índices de las columnas.
                val idIndex = cursor.getColumnIndex(KEY_ID)
                val nombreIndex = cursor.getColumnIndex(KEY_NOMBRE)
                val direccionIndex = cursor.getColumnIndex(KEY_DIRECCION)
                val valoracionIndex =cursor.getColumnIndex(KEY_VALORACION)
                val latlongIndex =cursor.getColumnIndex(KEY_LatitudLongitud)
                val webIndex =cursor.getColumnIndex(KEY_WEB)

                // Verifica que los índices sean válidos.
                if (idIndex != -1 && nombreIndex != -1 && direccionIndex != -1 && valoracionIndex != -1 && latlongIndex != -1 && webIndex != -1) {
                    // Lee los valores y los añade a la lista de Bars.
                    id = cursor.getInt(idIndex)
                    nombre = cursor.getString(nombreIndex)
                    direccion = cursor.getString(direccionIndex)
                    valoracion = cursor.getString(valoracionIndex)
                    latlong= cursor.getString(latlongIndex)
                    web= cursor.getString(webIndex)

                    val bar = Bar(id = id, nombreBar = nombre, direccion = direccion, valoracion = valoracion.toFloat(), latitudLongitud = latlong, web = web)
                    BaresList.add(bar)
                }
            } while (cursor.moveToNext())
        }

        // Cierra el cursor para liberar recursos.
        cursor.close()
        return BaresList
    }

    // Método para actualizar un Bar en la base de datos.
    fun updateBar(Bar: Bar): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        // Prepara los valores a actualizar.
        contentValues.put(KEY_NOMBRE, Bar.nombreBar)
        contentValues.put(KEY_DIRECCION, Bar.direccion)
        contentValues.put(KEY_VALORACION, Bar.valoracion)
        contentValues.put(KEY_LatitudLongitud, Bar.latitudLongitud)
        contentValues.put(KEY_WEB, Bar.web)

        // Actualiza la fila correspondiente y retorna el número de filas afectadas.
        return db.update(TABLE_BARES, contentValues, "$KEY_ID = ?", arrayOf(Bar.id.toString()))
    }

    // Método para eliminar un Bar de la base de datos.
    fun deleteBar(Bar: Bar): Int {
        val db = this.writableDatabase
        // Elimina la fila correspondiente y retorna el número de filas afectadas.
        val success = db.delete(TABLE_BARES, "$KEY_ID = ?", arrayOf(Bar.id.toString()))
        db.close()
        return success
    }

    // Método para añadir un nuevo Bar a la base de datos.
    fun addBar(Bar: Bar): Long {
        try {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            // Prepara los valores a insertar.
            contentValues.put(KEY_NOMBRE, Bar.nombreBar)
            contentValues.put(KEY_DIRECCION, Bar.direccion)
            contentValues.put(KEY_VALORACION, Bar.valoracion)
            contentValues.put(KEY_LatitudLongitud, Bar.latitudLongitud)
            contentValues.put(KEY_WEB, Bar.web)

            // Inserta el nuevo Bar y retorna el ID del nuevo Bar o -1 en caso de error.
            val success = db.insert(TABLE_BARES, null, contentValues)
            db.close()
            return success
        } catch (e: Exception) {
            // Maneja la excepción en caso de error al insertar.
            Log.e("Error", "Error al agregar Bar", e)
            return -1
        }
    }
}


