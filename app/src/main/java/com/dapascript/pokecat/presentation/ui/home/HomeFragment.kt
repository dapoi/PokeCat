package com.dapascript.pokecat.presentation.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dapascript.pokecat.R
import com.dapascript.pokecat.databinding.FragmentHomeBinding
import com.dapascript.pokecat.presentation.adapter.PokemonAdapter
import com.dapascript.pokecat.presentation.ui.BaseFragment
import com.dapascript.pokecat.presentation.ui.detail.DetailFragment
import com.dapascript.pokecat.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private lateinit var pokemonAdapter: PokemonAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initViewModel()
    }

    private fun initAdapter() {
        pokemonAdapter = PokemonAdapter(
            onClick = {
                val bundle = Bundle().apply {
                    putString(DetailFragment.POKEMON_NAME, it.name)
                }
                findNavController().navigate(
                    R.id.action_homeFragment_to_detailFragment,
                    bundle
                )
            }
        )
        binding.rvPokemon.apply {
            adapter = pokemonAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        homeViewModel.getPokemonList.observe(viewLifecycleOwner) { result ->
            binding.apply {
                progressBar.isVisible = result is Resource.Loading
                rvPokemon.isVisible = result is Resource.Success
                clFetchFailed.isVisible = result is Resource.Error
                if (result is Resource.Success) {
                    tvEmpty.isVisible = result.data.isEmpty()
                    pokemonAdapter.submitList(result.data)
                    initInteraction()
                }

                btnRetry.setOnClickListener {
                    homeViewModel.fetchPokemon()
                }
            }
        }
    }

    private fun initInteraction() {
        binding.apply {
            svPokemon.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val imm =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, 0)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    homeViewModel.searchPokemon(newText.toString())
                    return true
                }
            })

            ivSort.setOnClickListener { view ->
                val popupMenu = PopupMenu(requireContext(), view)
                popupMenu.menuInflater.inflate(R.menu.sort_menu, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener { item ->
                    val sortedList = when (item.itemId) {
                        R.id.asc -> pokemonAdapter.currentList.sortedBy { it.name }
                        R.id.desc -> pokemonAdapter.currentList.sortedByDescending { it.name }
                        else -> return@setOnMenuItemClickListener false
                    }

                    pokemonAdapter.submitList(sortedList) {
                        rvPokemon.scrollToPosition(0)
                    }

                    true
                }

                popupMenu.show()
            }
        }
    }
}