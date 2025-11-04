package br.com.pokedex.data

import br.com.pokedex.data.api.PokeApi
import br.com.pokedex.data.local.model.Generation
import br.com.pokedex.data.local.model.PokemonDetail
import br.com.pokedex.data.local.model.PokemonItem

class PokemonRepository(private val api: PokeApi) {

    suspend fun getPokemonList(limit: Int = 500): List<PokemonItem> {
        val res = api.listPokemon(limit)
        return res.results.map { item ->
            val id = item.url.trimEnd('/').split("/").last().toInt()
            PokemonItem(
                id = id,
                name = item.name.replaceFirstChar { it.uppercase() },
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
            )
        }
    }

    suspend fun getPokemonDetail(idOrName: String): PokemonDetail {
        val dto = api.getPokemon(idOrName)

        val imgUrl = dto.sprites.other?.officialArtwork?.frontDefault
            ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${dto.id}.png"

        val types = dto.types.sortedBy { it.slot }
            .map { it.type.name.replaceFirstChar { c -> c.uppercase() } }

        val statsMap: Map<String, Int> = dto.stats.associate { s ->
            s.stat.name.lowercase() to s.baseStat
        }

        return PokemonDetail(
            id = dto.id,
            name = dto.name.replaceFirstChar { it.uppercase() },
            imageUrl = imgUrl,
            types = types,
            height = dto.height,
            weight = dto.weight,
            stats = statsMap
        )
    }

    suspend fun getPokemonNamesByType(typeName: String): Set<String> {
        val res = api.getType(typeName.lowercase())
        return res.pokemon.map { it.pokemon.name }.toSet()
    }

    fun isFromGeneration(id: Int, gen: Generation): Boolean = id in gen.range
}
