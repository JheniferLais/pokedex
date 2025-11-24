package br.com.pokedex.data.local.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val height: Int,
    val weight: Int,
    val stats: Map<String, Int> // HP, ATK, DEF...
)
