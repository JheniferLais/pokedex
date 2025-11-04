package br.com.pokedex.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import br.com.pokedex.databinding.ItemPokemonBinding
import br.com.pokedex.data.local.model.PokemonItem

class PokemonAdapter(
    private val onClick: (PokemonItem) -> Unit
) : ListAdapter<PokemonItem, PokemonAdapter.VH>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<PokemonItem>() {
        override fun areItemsTheSame(oldItem: PokemonItem, newItem: PokemonItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: PokemonItem, newItem: PokemonItem) = oldItem == newItem
    }

    inner class VH(val binding: ItemPokemonBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PokemonItem) = with(binding) {
            name.text = "${item.name}"
            number.text = "#${item.id}"
            image.load(item.imageUrl) { crossfade(true) }
            root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}
