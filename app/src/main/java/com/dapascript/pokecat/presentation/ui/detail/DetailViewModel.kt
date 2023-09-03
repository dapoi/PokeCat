package com.dapascript.pokecat.presentation.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapascript.pokecat.data.repository.PokemonRepository
import com.dapascript.pokecat.data.source.remote.model.PokemonDetailResponse
import com.dapascript.pokecat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _getPokemonDetail = MutableLiveData<Resource<PokemonDetailResponse>>()
    val getPokemonDetail: LiveData<Resource<PokemonDetailResponse>> = _getPokemonDetail

    fun fetchPokemonDetail(name: String) {
        viewModelScope.launch {
            pokemonRepository.getPokemonDetail(name).collect { response ->
                _getPokemonDetail.value = response
            }
        }
    }
}