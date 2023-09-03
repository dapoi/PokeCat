package com.dapascript.pokecat.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dapascript.pokecat.data.source.local.model.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon")
    fun getAllPokemon(): Flow<List<PokemonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: List<PokemonEntity>)

    // query to get pokemon by name
    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :name || '%'")
    fun getPokemonByName(name: String): Flow<List<PokemonEntity>>
}