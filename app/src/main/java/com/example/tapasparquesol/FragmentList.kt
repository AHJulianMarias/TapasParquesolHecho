package com.example.tapasparquesol

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tapasparquesol.adapter.BarAdapter
import com.example.tapasparquesol.dataClass.Bar
import com.example.tapasparquesol.helper.NotificationHelper
import com.example.tapasparquesol.helper.dbHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentList: Fragment() {

    private lateinit var dbHandler: dbHelper
    private lateinit var listView: ListView
    private lateinit var adapter: BarAdapter
    private lateinit var buttonAnadir: Button
    private lateinit var notificationHelper: NotificationHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lista_bares, container, false)
        listView = view.findViewById(R.id.ListView)
        dbHandler = dbHelper(requireContext())
        buttonAnadir = view.findViewById(R.id.anadirDesdeLista)

        notificationHelper = NotificationHelper(requireContext())
        notificationHelper.requestNotificationPermission(requireContext())


        buttonAnadir.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_anadir)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.setCancelable(false)
            dialog.show()
            val buttonSalir = dialog.findViewById<Button>(R.id.buttonCancelarAnadir)
            buttonSalir.setOnClickListener {
                dialog.dismiss()
            }
            val etNombre = dialog.findViewById<EditText>(R.id.etNombreBar)
            val etDireccion = dialog.findViewById<EditText>(R.id.etDireccion)
            val etLongLat = dialog.findViewById<EditText>(R.id.etlongitudlatitud)
            val etweb = dialog.findViewById<EditText>(R.id.etweb)
            val buttonConfirmar = dialog.findViewById<Button>(R.id.buttonConfirmarAnadir)
            buttonConfirmar.setOnClickListener {

                if (
                    etDireccion.text.toString().isEmpty() ||
                    etLongLat.text.toString().isEmpty() ||
                    etweb.text.toString().isEmpty()
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Por favor, rellena todos los campos",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                val bar = Bar(
                    nombreBar = etNombre.text.toString(),
                    direccion = etDireccion.text.toString(),
                    valoracion = 0.0F,
                    latitudLongitud = etLongLat.text.toString(),
                    web = etweb.text.toString()
                )

                CoroutineScope(Dispatchers.Main).launch {
                    val status = withContext(Dispatchers.IO) {
                        dbHandler.addBar(bar)
                    }
                    if (status > -1) {
                        Toast.makeText(requireContext(), "Bar agregado", Toast.LENGTH_LONG).show()
                        adapter.updateData(dbHandler.getAllBares())
                        notificationHelper.showNotification(bar.nombreBar, bar.web)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Error en la inserción", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBares()

    }
    //Sin corrutinas quitas lo de alrededor y ya
    private fun loadBares() {
        CoroutineScope(Dispatchers.Main).launch {
            adapter = BarAdapter(requireContext(), dbHandler.getAllBares())
            listView.adapter = adapter
        }
    }


//    private fun addBar(bar: Bar) {
//        CoroutineScope(Dispatchers.Main).launch {
//            val status = dbHandler.addBar(bar)
//            if (status > -1) {
//                Toast.makeText(requireContext(), "Bar agregado", Toast.LENGTH_LONG).show()
//            } else {
//                // Muestra un mensaje si los campos están vacíos.
//                Toast.makeText(requireContext(), "Error en la inserción", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
}
