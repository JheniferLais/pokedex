package br.com.pokedex.data.api

import com.squareup.moshi.Json

// https://pokeapi.co/api/v2/pokemon
data class PokemonListResponse(val results: List<NamedApiResource>)
data class NamedApiResource(val name: String, val url: String)

data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<TypeSlot>,
    val stats: List<StatDto>,
    val sprites: SpritesDto
)

data class TypeSlot(val slot: Int, val type: NamedApiResource)
data class StatDto(@Json(name = "base_stat") val baseStat: Int, val stat: NamedApiResource)

data class SpritesDto(val other: OtherSprites? = null)

data class OtherSprites(
    @Json(name = "official-artwork") val officialArtwork: OfficialArtwork? = null
)

data class OfficialArtwork(@Json(name = "front_default") val frontDefault: String?)

data class TypeResponse(
    val name: String,
    val pokemon: List<TypePokemon>
)

data class TypePokemon(val pokemon: NamedApiResource)
