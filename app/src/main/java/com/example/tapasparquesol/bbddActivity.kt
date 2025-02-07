package com.example.tapasparquesol

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class bbddActivity: AppCompatActivity()  {
    private lateinit var etNombre: EditText
    private lateinit var etDireccion: EditText
    private lateinit var btnAgregar: Button
    private lateinit var btnVerTodos: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnModificar: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHandler: dbHelper
    private lateinit var ratingBar: RatingBar
    private lateinit var etWeb: EditText
    private lateinit var longlat: EditText
    private val REQUEST_CODE_PERMISSIONS = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bbdd_layout)

        etNombre = findViewById(R.id.etNombreBar)
        etDireccion = findViewById(R.id.etDireccion)
        ratingBar = findViewById(R.id.ratingBar)
        btnAgregar = findViewById(R.id.btnAdd)
        btnEliminar = findViewById(R.id.btnDel)
        btnModificar = findViewById(R.id.btnModif)
        btnAgregar = findViewById(R.id.btnAdd)
        btnVerTodos = findViewById(R.id.btnViewAll)
        dbHandler = dbHelper(this)
        longlat = findViewById(R.id.etlongitudlatitud)
        etWeb = findViewById(R.id.etweb)
        // Configura los eventos de clic para los botones.
        btnAgregar.setOnClickListener {
           addBar()
        }
//        btnVerTodos.setOnClickListener { viewBares() }
//        btnEliminar.setOnClickListener{
//            if(deleteBar()) mediaPlayer.start()}
//        btnModificar.setOnClickListener{
//            if(modifyBar()) mediaPlayer.start()}
        // Muestra la lista de Gatos al iniciar la actividad.
        viewBar()


        val botonCambiarFragment = findViewById<Button>(R.id.buttonVolver)
        botonCambiarFragment.setOnClickListener{

            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)


        }

    }
    // Método para mostrar todos los Gatos en el RecyclerView.
    private fun viewBar() {
        // Obtiene la lista de Gatos de la base de datos.
        val baresList = dbHandler.getAllBares()
        // Crea un adaptador para el RecyclerView y lo configura.
        val adapter = barAdapterBBDD(baresList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    // Método para limpiar los campos de texto.
    private fun clearEditTexts() {
        etNombre.text.clear()
        etDireccion.text.clear()

    }
    private fun addBar():Boolean {
        // Obtiene el texto de los EditText y lo convierte en String.
        val nombre = etNombre.text.toString()
        val direccion = etDireccion.text.toString()
        val valoracion = ratingBar.rating
        val logitudlatitud = longlat.text
        val web = etWeb.text.toString()
        // Verifica que los campos no estén vacíos.
        if (nombre.isNotEmpty() && direccion.isNotEmpty()) {
            // Crea un objeto Gato y lo añade a la base de datos.
            val bar = Bar(id = 1, nombreBar = nombre, direccion = direccion, valoracion = valoracion, web = web, latitudLongitud = logitudlatitud.toString())
            val status = dbHandler.addBar(bar)
            // Verifica si la inserción fue exitosa.
            if (status > -1) {
                Toast.makeText(applicationContext, "Bar agregado", Toast.LENGTH_LONG).show()
                // Limpia los campos de texto y actualiza la vista de Gatos.
                clearEditTexts()
                viewBar()
                return true
            }
        } else {
            // Muestra un mensaje si los campos están vacíos.
            Toast.makeText(applicationContext, "Nombre y color son requeridos", Toast.LENGTH_LONG).show()
            return false
        }
        return false
    }


    private fun showNotification(title: String, message: String) {
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, "canal_notificaciones")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Icono de la notificación
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // La notificación desaparece al tocarla

        notificationManager.notify(1, builder.build()) // ID de la notificación
    }
    private fun checkPermissions(): Boolean {
        val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true // No es necesario pedir permiso en versiones anteriores
        }
        return notificationPermission
    }
    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    // Maneja la respuesta del usuario cuando acepta o niega permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, mostrar notificación
                showNotification("¡Permiso concedido!", "Ahora recibirás notificaciones.")
            } else {
                // Permiso denegado, mostrar mensaje
                showNotification("Permiso denegado", "No puedes recibir notificaciones.")
            }
        }
    }
}