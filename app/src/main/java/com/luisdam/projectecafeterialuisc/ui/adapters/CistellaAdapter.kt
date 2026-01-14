package com.luisdam.projectecafeterialuisc.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luisdam.projectecafeterialuisc.databinding.ItemCistellaBinding
import com.luisdam.projectecafeterialuisc.model.Producte

class CistellaAdapter : RecyclerView.Adapter<CistellaAdapter.CistellaViewHolder>() {

    private var productes: List<Producte> = emptyList()

    inner class CistellaViewHolder(private val binding: ItemCistellaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producte: Producte) {
            binding.tvProducteNom.text = producte.nom
            binding.tvProductePreu.text = "%.2f€".format(producte.preu)
            binding.tvCategoria.text = producte.categoria.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CistellaViewHolder {
        val binding = ItemCistellaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CistellaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CistellaViewHolder, position: Int) {
        holder.bind(productes[position])
    }

    override fun getItemCount() = productes.size

    fun updateProductes(newProductes: List<Producte>) {
        productes = newProductes
        notifyDataSetChanged()
    }
}