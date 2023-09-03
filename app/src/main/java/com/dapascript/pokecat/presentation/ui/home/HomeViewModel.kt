package com.dapascript.pokecat.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapascript.pokecat.data.repository.PokemonRepository
import com.dapascript.pokecat.data.source.local.model.PokemonEntity
import com.dapascript.pokecat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _getPokemonList = MutableLiveData<Resource<List<PokemonEntity>>>()
    val getPokemonList: LiveData<Resource<List<PokemonEntity>>> = _getPokemonList

    init {
        fetchPokemon()
    }

    fun fetchPokemon() {
        viewModelScope.launch {
            pokemonRepository.getPokemonList().collect { response ->
                _getPokemonList.value = response
            }
        }
    }

    // query to get pokemon by name
    fun searchPokemon(name: String) {
        viewModelScope.launch {
            pokemonRepository.getPokemonByName(name).collect { response ->
                _getPokemonList.value = Resource.Success(response)
            }
        }
    }
}