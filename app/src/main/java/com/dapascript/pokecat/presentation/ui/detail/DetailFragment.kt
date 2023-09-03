package com.dapascript.pokecat.presentation.ui.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dapascript.pokecat.databinding.FragmentDetailBinding
import com.dapascript.pokecat.presentation.adapter.PokemonAbilityAdapter
import com.dapascript.pokecat.presentation.ui.BaseFragment
import com.dapascript.pokecat.utils.Resource
import com.dapascript.pokecat.utils.capitalizeFirstLetter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private val abilityAdapter: PokemonAbilityAdapter by lazy { PokemonAbilityAdapter() }
    private val detailViewModel: DetailViewModel by viewModels()
    private var pokemonName: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pokemonName = arguments?.getString(POKEMON_NAME)
        binding.toolbar.apply {
            title = capitalizeFirstLetter(pokemonName.toString())
            setTitleTextColor(resources.getColor(android.R.color.white, null))
            setNavigationOnClickListener { findNavController().navigateUp() }
        }

        initAdapter()
        initViewModel()
    }

    private fun initAdapter() {
        binding.rvAbilities.apply {
            adapter = abilityAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initViewModel() {
        detailViewModel.apply {
            fetchPokemonDetail(pokemonName.toString())
            getPokemonDetail.observe(viewLifecycleOwner) { response ->
                binding.apply {
                    progressBar.isVisible = response is Resource.Loading
                    clContent.isVisible = response is Resource.Success
                    clFetchFailed.isVisible = response is Resource.Error
                    if (response is Resource.Success) {
                        Glide.with(requireContext())
                            .load(response.data.sprites?.frontDefault)
                            .into(ivPokemon)

                        abilityAdapter.submitList(response.data.abilities)
                        tvName.text = capitalizeFirstLetter(response.data.name.toString())
                    }

                    btnRetry.setOnClickListener {
                        fetchPokemonDetail(pokemonName.toString())
                    }
                }
            }
        }
    }

    companion object {
        const val POKEMON_NAME = "pokemon_name"
    }
}