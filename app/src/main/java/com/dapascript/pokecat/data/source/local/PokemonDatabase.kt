package com.dapascript.pokecat.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dapascript.pokecat.data.source.local.model.PokemonEntity

@Database(
    entities = [PokemonEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}