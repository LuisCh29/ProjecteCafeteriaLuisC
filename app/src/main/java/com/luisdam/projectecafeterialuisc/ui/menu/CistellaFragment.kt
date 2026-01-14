package com.luisdam.projectecafeterialuisc.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.luisdam.projectecafeterialuisc.databinding.FragmentCistellaBinding
import com.luisdam.projectecafeterialuisc.ui.adapters.CistellaAdapter
import com.luisdam.projectecafeterialuisc.viewmodel.SharedViewModel

class CistellaFragment : Fragment() {

    private var _binding: FragmentCistellaBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var cistellaAdapter: CistellaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCistellaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        observeData()
    }

    private fun setupRecyclerView() {
        cistellaAdapter = CistellaAdapter()

        binding.rvCistella.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cistellaAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupListeners() {
        binding.btnComprar.setOnClickListener {
            comprarCistella()
        }

        binding.btnBuidar.setOnClickListener {
            buidarCistella()
        }
    }

    private fun observeData() {
        sharedViewModel.cistellaItems.observe(viewLifecycleOwner) { items ->
            if (items.isNotEmpty()) {
                binding.tvEmpty.visibility = View.GONE
                binding.rvCistella.visibility = View.VISIBLE
                binding.llTotal.visibility = View.VISIBLE

                cistellaAdapter.updateProductes(items)
                updateTotal()
            } else {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvCistella.visibility = View.GONE
                binding.llTotal.visibility = View.GONE
            }
        }

        sharedViewModel.usuariActual.observe(viewLifecycleOwner) { usuari ->
            binding.tvUsuari.text = "Usuari: $usuari"
        }
    }

    private fun updateTotal() {
        val total = sharedViewModel.getPreuTotal()
        val quantitat = sharedViewModel.getQuantitatTotal()

        binding.tvTotal.text = "Total: %.2f€".format(total)
        binding.tvQuantitat.text = "Items: $quantitat"
    }

    private fun comprarCistella() {
        val total = sharedViewModel.getPreuTotal()
        if (total > 0) {
            Toast.makeText(
                requireContext(),
                "Compra realitzada per %.2f€".format(total),
                Toast.LENGTH_LONG
            ).show()
            sharedViewModel.buidarCistella()
        } else {
            Toast.makeText(requireContext(), "La cistella està buida", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buidarCistella() {
        sharedViewModel.buidarCistella()
        Toast.makeText(requireContext(), "Cistella buidada", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}