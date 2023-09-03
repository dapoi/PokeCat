package com.dapascript.pokecat.data.source.remote.model

import com.squareup.moshi.Json

data class PokemonResponse(

	@Json(name="results")
	val results: List<ResultsItem?>? = null
)

data class ResultsItem(

	@Json(name="name")
	val name: String? = null,

	@Json(name="url")
	val url: String? = null
)
