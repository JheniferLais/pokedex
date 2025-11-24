package br.com.pokedex.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {
    @GET("pokemon")
    suspend fun listPokemon(@Query("limit") limit: Int = 100): PokemonListResponse

    @GET("pokemon/{idOrName}")
    suspend fun getPokemon(@Path("idOrName") idOrName: String): PokemonDetailDto

    @GET("type/{typeName}")
    suspend fun getType(@Path("typeName") typeName: String): TypeResponse
}
