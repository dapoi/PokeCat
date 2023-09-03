package com.dapascript.pokecat.presentation.adapter

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dapascript.pokecat.data.source.remote.model.AbilitiesItem
import com.dapascript.pokecat.databinding.ItemListAbilityBinding
import com.dapascript.pokecat.utils.capitalizeFirstLetter
import java.util.Random

class PokemonAbilityAdapter :
    ListAdapter<AbilitiesItem, PokemonAbilityAdapter.PokemonAbilityViewHolder>(PokemonAbilityAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonAbilityViewHolder {
        return PokemonAbilityViewHolder(
            ItemListAbilityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PokemonAbilityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PokemonAbilityViewHolder(
        private val binding: ItemListAbilityBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: AbilitiesItem) {
            binding.apply {
                val random = Random()
                val progressAbility = item.ability?.url?.split("/")?.toTypedArray()?.get(6)
                val targetProgress = progressAbility?.toInt() ?: 0
                val currentProgress = pbAbility.progress
                val animator = ValueAnimator.ofInt(currentProgress, targetProgress)
                animator.duration = 1000 // Durasi animasi dalam milidetik (1 detik)

                animator.addUpdateListener { animation ->
                    val progress = animation.animatedValue as Int
                    pbAbility.progress = progress
                    tvAbilityProgress.text = "$progress%"
                }

                pbAbility.progressTintList = ColorStateList.valueOf(
                    Color.argb(
                        255,
                        random.nextInt(256),
                        random.nextInt(256),
                        random.nextInt(256)
                    )
                )

                tvAbilityName.text = capitalizeFirstLetter(item.ability?.name.toString())

                // Mulai animasi
                animator.start()
            }
        }
    }


    companion object : DiffUtil.ItemCallback<AbilitiesItem>() {
        override fun areItemsTheSame(oldItem: AbilitiesItem, newItem: AbilitiesItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AbilitiesItem, newItem: AbilitiesItem): Boolean {
            return oldItem == newItem
        }
    }
}