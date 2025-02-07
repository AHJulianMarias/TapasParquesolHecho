package com.example.tapasparquesol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class barAdapterBBDD (private val baresList: List<Bar>) : RecyclerView.Adapter<barAdapterBBDD.MyViewHolder>(){

    // Define la clase interna MyViewHolder, que extiende RecyclerView.ViewHolder.
    // Esta clase proporciona una referencia a las vistas de cada elemento de datos.
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Encuentra y almacena referencias a los elementos de la interfaz de usuario en el layout del ítem.
        var nombreBar: TextView = view.findViewById(R.id.tvNombreBar)
        var valoracion: RatingBar = view.findViewById(R.id.ratingBarLista)
    }

    // Crea nuevas vistas (invocadas por el layout manager).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Infla el layout del ítem de la lista (disco_item.xml) y lo pasa al constructor de MyViewHolder.
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_bar, parent, false)
        return MyViewHolder(itemView)
    }

    // Reemplaza el contenido de una vista (invocada por el layout manager).
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Obtiene el elemento de la lista de discos en esta posición.
        val bar = baresList[position]
        // Reemplaza el contenido de las vistas con los datos del elemento en cuestión.
        holder.nombreBar.text = bar.nombreBar
        holder.valoracion.rating = bar.valoracion
    }

    // Devuelve el tamaño de la lista de datos (invocado por el layout manager).
    override fun getItemCount() = baresList.size
}