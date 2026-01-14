package com.luisdam.projectecafeterialuisc.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luisdam.projectecafeterialuisc.databinding.ItemProducteBinding
import com.luisdam.projectecafeterialuisc.model.CategoriaProducte
import com.luisdam.projectecafeterialuisc.model.Producte
import com.luisdam.projectecafeterialuisc.R

class ProducteAdapter(
    private val onAddToCart: (Producte) -> Unit
) : RecyclerView.Adapter<ProducteAdapter.ProducteViewHolder>() {

    private var productes: List<Producte> = emptyList()

    inner class ProducteViewHolder(private val binding: ItemProducteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producte: Producte) {
            binding.tvProducteNom.text = producte.nom
            binding.tvProducteDescripcio.text = producte.descripcio
            binding.tvProductePreu.text = "%.2f€".format(producte.preu)

            val imageResId = producte.imageResId ?: 0

            if (imageResId != 0) {
                binding.ivProducte.setImageResource(imageResId)
            } else {
                val defaultImage = when (producte.categoria) {
                    CategoriaProducte.MENJAR -> R.drawable.menjar
                    CategoriaProducte.BEGUDES -> R.drawable.beguda
                    CategoriaProducte.POSTRES -> R.drawable.postres
                }
                binding.ivProducte.setImageResource(defaultImage)
            }

            binding.btnAddToCart.setOnClickListener {
                onAddToCart(producte)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProducteViewHolder {
        val binding = ItemProducteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProducteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProducteViewHolder, position: Int) {
        holder.bind(productes[position])
    }

    override fun getItemCount() = productes.size

    fun updateProductes(newProductes: List<Producte>) {
        productes = newProductes
        notifyDataSetChanged()
    }
}