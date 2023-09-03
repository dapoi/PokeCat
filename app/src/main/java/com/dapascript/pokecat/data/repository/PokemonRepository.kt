package com.dapascript.pokecat.data.repository

import com.dapascript.pokecat.data.source.local.PokemonDao
import com.dapascript.pokecat.data.source.local.model.PokemonEntity
import com.dapascript.pokecat.data.source.remote.ApiService
import com.dapascript.pokecat.data.source.remote.model.PokemonDetailResponse
import com.dapascript.pokecat.utils.Resource
import com.dapascript.pokecat.utils.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override fun getPokemonList(): Flow<Resource<List<PokemonEntity>>> {
        return networkBoundResource(
            query = {
                pokemonDao.getAllPokemon()
            },
            fetch = {
                apiService.getPokemonList()
            },
            saveFetchResult = { pokemonList ->
                val local = ArrayList<PokemonEntity>()

                // map response to entity
                pokemonList.results?.map { response ->
                    val pokemonEntity = PokemonEntity(
                        name = response?.name.toString(),
                        url = response?.url.toString()
                    )
                    local.add(pokemonEntity)
                }

                // insert entity to database
                pokemonDao.insertPokemon(local)
            }
        )
    }

    override fun getPokemonByName(name: String): Flow<List<PokemonEntity>> {
        return pokemonDao.getPokemonByName(name)
    }

    override fun getPokemonDetail(name: String): Flow<Resource<PokemonDetailResponse>> {
        return flow {
            emit(Resource.Loading)

            try {
                val response = apiService.getPokemonDetail(name)
                emit(Resource.Success(response))
            } catch (e: Throwable) {
                emit(Resource.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}

interface PokemonRepository {

    fun getPokemonList(): Flow<Resource<List<PokemonEntity>>>
    fun getPokemonByName(name: String): Flow<List<PokemonEntity>>
    fun getPokemonDetail(name: String): Flow<Resource<PokemonDetailResponse>>
}