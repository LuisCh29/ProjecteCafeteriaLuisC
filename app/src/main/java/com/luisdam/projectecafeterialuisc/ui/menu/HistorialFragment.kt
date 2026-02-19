package com.luisdam.projectecafeterialuisc.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luisdam.projectecafeterialuisc.data.firebase.FirebaseAuthService
import com.luisdam.projectecafeterialuisc.data.firebase.FirestoreService
import com.luisdam.projectecafeterialuisc.databinding.FragmentHistorialBinding
import com.luisdam.projectecafeterialuisc.model.Comanda
import com.luisdam.projectecafeterialuisc.ui.adapters.HistorialAdapter
import com.luisdam.projectecafeterialuisc.viewmodel.SharedViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HistorialFragment : Fragment() {

    private var _binding: FragmentHistorialBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var historialAdapter: HistorialAdapter
    private val firestoreService = FirestoreService()
    private val authService = FirebaseAuthService()

    private var comandesList: List<Comanda> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupUserInfo()
        carregarHistorial()
    }

    private fun setupRecyclerView() {
        historialAdapter = HistorialAdapter(
            onItemClick = { comanda ->
                mostrarDetallesComanda(comanda)
            },
            onDeleteClick = { comandaId ->
                mostrarDialogoEliminar(comandaId)
            }
        )

        binding.rvHistorial.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historialAdapter
            setHasFixedSize(true)
            addItemDecoration(
                androidx.recyclerview.widget.DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            carregarHistorial()
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }

        binding.btnClearAll.setOnClickListener {
            mostrarDialogoEliminarTodas()
        }
    }

    private fun setupUserInfo() {
        val currentUser = authService.currentUser
        currentUser?.let { user ->
            val nom = authService.currentUserName ?: user.email?.split("@")?.first() ?: "Usuari"
            binding.tvUserName.text = "Hola, $nom"
        } ?: run {
            binding.tvUserName.text = "Usuari no identificat"
        }
    }

    private fun carregarHistorial() {
        val usuariId = sharedViewModel.usuariId.value

        if (usuariId.isNullOrEmpty()) {
            mostrarMensajeNoAutenticado()
            return
        }

        binding.swipeRefresh.isRefreshing = true
        binding.tvEmpty.visibility = View.GONE

        lifecycleScope.launch {
            val result = firestoreService.obtenirComandesUsuari(usuariId)

            binding.swipeRefresh.isRefreshing = false

            if (result.isSuccess) {
                comandesList = result.getOrThrow()

                if (comandesList.isNotEmpty()) {
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvHistorial.visibility = View.VISIBLE
                    historialAdapter.actualizarComandes(comandesList)

                    calcularEstadisticas()
                } else {
                    mostrarMensajeVacio()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error al carregar l'historial: ${result.exceptionOrNull()?.message}",
                    Toast.LENGTH_LONG
                ).show()
                mostrarMensajeError()
            }
        }
    }

    private fun calcularEstadisticas() {
        val totalGastat = comandesList.sumOf { it.total }
        binding.tvStats.text = "${comandesList.size} comandes | ${"%.2f".format(totalGastat)}€ gastats"
    }

    private fun mostrarDetallesComanda(comanda: Comanda) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val fecha = dateFormat.format(comanda.data)

        val productesText = comanda.productes.joinToString("\n") { producto ->
            "  • ${producto.nom} x${producto.quantitat} - ${"%.2f".format(producto.preu)}€"
        }

        val mensaje = """
            |Detalls de la comanda
            |─────────────────────
            |ID: ${comanda.id.take(8)}
            |Data: $fecha
            |Estat: ${comanda.estat}
            |Total: ${"%.2f".format(comanda.total)}€
            
            |Productes:
            |$productesText
        """.trimMargin()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Detalls de la comanda")
            .setMessage(mensaje)
            .setPositiveButton("Tancar") { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("Eliminar") { dialog, _ ->
                mostrarDialogoEliminar(comanda.id)
                dialog.dismiss()
            }
            .show()
    }

    private fun mostrarDialogoEliminar(comandaId: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar comanda")
            .setMessage("Estàs segur que vols eliminar aquesta comanda?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                eliminarComanda(comandaId)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel·lar", null)
            .show()
    }

    private fun mostrarDialogoEliminarTodas() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar totes les comandes")
            .setMessage("Aquesta acció eliminarà tot l'historial. Estàs segur?")
            .setPositiveButton("Eliminar tot") { dialog, _ ->
                eliminarTodasComandes()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel·lar", null)
            .show()
    }

    private fun eliminarComanda(comandaId: String) {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE

            val result = firestoreService.eliminarComanda(comandaId)

            binding.progressBar.visibility = View.GONE

            if (result.isSuccess) {
                Toast.makeText(requireContext(), "Comanda eliminada", Toast.LENGTH_SHORT).show()
                carregarHistorial()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error: ${result.exceptionOrNull()?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun eliminarTodasComandes() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE

            val results = comandesList.map { comanda ->
                firestoreService.eliminarComanda(comanda.id)
            }

            binding.progressBar.visibility = View.GONE

            val exitos = results.count { it.isSuccess }
            if (exitos > 0) {
                Toast.makeText(
                    requireContext(),
                    "Eliminades $exitos comandes",
                    Toast.LENGTH_SHORT
                ).show()
                carregarHistorial()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error en eliminar les comandes",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun logout() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Tancar sessió")
            .setMessage("Estàs segur que vols tancar la sessió?")
            .setPositiveButton("Sí") { dialog, _ ->
                authService.logout()
                sharedViewModel.logout()
                requireActivity().finish()
                dialog.dismiss()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun mostrarMensajeNoAutenticado() {
        binding.tvEmpty.text = "Has d'iniciar sessió per veure l'historial"
        binding.tvEmpty.visibility = View.VISIBLE
        binding.rvHistorial.visibility = View.GONE
        binding.llStats.visibility = View.GONE
        binding.btnClearAll.visibility = View.GONE
    }

    private fun mostrarMensajeVacio() {
        binding.tvEmpty.text = "No tens cap comanda encara"
        binding.tvEmpty.visibility = View.VISIBLE
        binding.rvHistorial.visibility = View.GONE
        binding.llStats.visibility = View.VISIBLE
        binding.tvStats.text = "0 comandes | 0.00€ gastats"
    }

    private fun mostrarMensajeError() {
        binding.tvEmpty.text = "Error al carregar l'historial"
        binding.tvEmpty.visibility = View.VISIBLE
        binding.rvHistorial.visibility = View.GONE
        binding.llStats.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        carregarHistorial()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}