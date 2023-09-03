package com.dapascript.pokecat.presentation.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dapascript.pokecat.data.source.local.model.PokemonEntity
import com.dapascript.pokecat.databinding.ItemListPokemonBinding
import com.dapascript.pokecat.utils.capitalizeFirstLetter
import java.util.Random

class PokemonAdapter(
    private val onClick: (PokemonEntity) -> Unit
) : ListAdapter<PokemonEntity, PokemonAdapter.PokemonViewHolder>(PokemonAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(
            ItemListPokemonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PokemonViewHolder(
        private val binding: ItemListPokemonBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(pokemon: PokemonEntity) {
            binding.apply {
                // set image pokemon
                val id = pokemon.url.split("/".toRegex()).dropLast(1).last()
                Glide.with(itemView)
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png")
                    .into(ivPokemon)

                // set name pokemon
                tvPokemonName.text = capitalizeFirstLetter(pokemon.name)

                // set random color for background card
                val random = Random()
                cvPokemon.backgroundTintList = ColorStateList.valueOf(
                    Color.argb(
                        200,
                        random.nextInt(256),
                        random.nextInt(256),
                        random.nextInt(256)
                    )
                )
            }

            itemView.setOnClickListener {
                onClick(pokemon)
            }
        }
    }

    companion object : DiffUtil.ItemCallback<PokemonEntity>() {
        override fun areItemsTheSame(oldItem: PokemonEntity, newItem: PokemonEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PokemonEntity, newItem: PokemonEntity): Boolean {
            return oldItem == newItem
        }
    }
}