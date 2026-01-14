package com.luisdam.projectecafeterialuisc.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.luisdam.projectecafeterialuisc.data.db.AppDatabase
import com.luisdam.projectecafeterialuisc.data.repository.ProducteRepository
import com.luisdam.projectecafeterialuisc.databinding.FragmentMenjarBinding
import com.luisdam.projectecafeterialuisc.ui.adapters.ProducteAdapter
import com.luisdam.projectecafeterialuisc.viewmodel.MenjarViewModel
import com.luisdam.projectecafeterialuisc.viewmodel.SharedViewModel

class MenjarFragment : Fragment() {

    private var _binding: FragmentMenjarBinding? = null
    private val binding get() = _binding!!

    private lateinit var menjarViewModel: MenjarViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var producteAdapter: ProducteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenjarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModels()
        setupRecyclerView()
        observeData()
    }

    private fun initViewModels() {
        val database = AppDatabase.getDatabase(requireContext())
        val repository = ProducteRepository(database)

        menjarViewModel = ViewModelProvider(
            this,
            MenuViewModelFactory(repository)
        )[MenjarViewModel::class.java]
    }

    private fun setupRecyclerView() {
        producteAdapter = ProducteAdapter { producte ->
            val producteModel = com.luisdam.projectecafeterialuisc.model.Producte(
                id = producte.id,
                nom = producte.nom,
                descripcio = producte.descripcio,
                preu = producte.preu,
                categoria = producte.categoria
            )
            sharedViewModel.afegirACistella(producteModel)
        }

        binding.rvMenjar.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = producteAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeData() {
        menjarViewModel.menjarProductes.observe(viewLifecycleOwner) { productes ->
            if (productes.isNotEmpty()) {
                binding.tvEmpty.visibility = View.GONE
                binding.rvMenjar.visibility = View.VISIBLE

                val productesList = productes.map { producteEntity ->
                    com.luisdam.projectecafeterialuisc.model.Producte(
                        id = producteEntity.id,
                        nom = producteEntity.nom,
                        descripcio = producteEntity.descripcio,
                        preu = producteEntity.preu,
                        categoria = producteEntity.categoria
                    )
                }
                producteAdapter.updateProductes(productesList)
            } else {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvMenjar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}