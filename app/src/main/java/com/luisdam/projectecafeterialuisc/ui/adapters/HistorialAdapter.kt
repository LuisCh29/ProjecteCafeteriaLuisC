package com.luisdam.projectecafeterialuisc.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.luisdam.projectecafeterialuisc.databinding.ItemHistorialBinding
import com.luisdam.projectecafeterialuisc.model.Comanda
import java.text.SimpleDateFormat
import java.util.Locale

class HistorialAdapter(
    private val onItemClick: (Comanda) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder>() {

    private var comandes: List<Comanda> = emptyList()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    inner class HistorialViewHolder(private val binding: ItemHistorialBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comanda: Comanda) {
            binding.tvComandaId.text = "Comanda #${comanda.id.take(8)}"
            binding.tvComandaData.text = dateFormat.format(comanda.data)
            binding.tvComandaTotal.text = "%.2f€".format(comanda.total)
            binding.tvComandaProductes.text = "${comanda.productes.size} productes"

            binding.tvComandaEstat.text = comanda.estat

            when (comanda.estat.toLowerCase(Locale.getDefault())) {
                "completada" -> {
                    binding.tvComandaEstat.setTextColor(
                        ContextCompat.getColor(binding.root.context, android.R.color.holo_green_dark)
                    )
                }
                "pendent" -> {
                    binding.tvComandaEstat.setTextColor(
                        ContextCompat.getColor(binding.root.context, android.R.color.holo_orange_dark)
                    )
                }
                "cancel·lada" -> {
                    binding.tvComandaEstat.setTextColor(
                        ContextCompat.getColor(binding.root.context, android.R.color.holo_red_dark)
                    )
                }
                else -> {
                    binding.tvComandaEstat.setTextColor(
                        ContextCompat.getColor(binding.root.context, android.R.color.darker_gray)
                    )
                }
            }

            binding.root.setOnClickListener {
                onItemClick(comanda)
            }

            binding.btnEliminar.setOnClickListener {
                onDeleteClick(comanda.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val binding = ItemHistorialBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistorialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        holder.bind(comandes[position])
    }

    override fun getItemCount() = comandes.size

    fun actualizarComandes(nuevasComandes: List<Comanda>) {
        comandes = nuevasComandes.sortedByDescending { it.data }
        notifyDataSetChanged()
    }
}