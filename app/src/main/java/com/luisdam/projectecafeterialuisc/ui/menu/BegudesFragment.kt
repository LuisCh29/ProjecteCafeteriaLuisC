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
import com.luisdam.projectecafeterialuisc.databinding.FragmentBegudesBinding
import com.luisdam.projectecafeterialuisc.ui.adapters.ProducteAdapter
import com.luisdam.projectecafeterialuisc.viewmodel.BegudesViewModel
import com.luisdam.projectecafeterialuisc.viewmodel.SharedViewModel

class BegudesFragment : Fragment() {

    private var _binding: FragmentBegudesBinding? = null
    private val binding get() = _binding!!

    private lateinit var begudesViewModel: BegudesViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var producteAdapter: ProducteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBegudesBinding.inflate(inflater, container, false)
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

        begudesViewModel = ViewModelProvider(
            this,
            MenuViewModelFactory(repository)
        )[BegudesViewModel::class.java]
    }

    private fun setupRecyclerView() {
        producteAdapter = ProducteAdapter { producte ->
            sharedViewModel.afegirACistella(producte)
        }

        binding.rvBegudes.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = producteAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeData() {
        begudesViewModel.begudesProductes.observe(viewLifecycleOwner) { productes ->
            if (productes.isNotEmpty()) {
                binding.tvEmpty.visibility = View.GONE
                binding.rvBegudes.visibility = View.VISIBLE

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
                binding.rvBegudes.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}