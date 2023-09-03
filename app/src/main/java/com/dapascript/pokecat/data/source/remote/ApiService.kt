package com.dapascript.pokecat.data.source.remote

import com.dapascript.pokecat.data.source.remote.model.PokemonDetailResponse
import com.dapascript.pokecat.data.source.remote.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("pokemon")
    suspend fun getPokemonList(): PokemonResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(
        @Path("name") name: String
    ): PokemonDetailResponse
}