package br.com.pokedex.view.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pokedex.data.PokemonRepository
import br.com.pokedex.data.local.model.Generation
import br.com.pokedex.data.local.model.PokemonItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    val loading: Boolean = false,
    val all: List<PokemonItem> = emptyList(),
    val visible: List<PokemonItem> = emptyList(),
    val searchQuery: String = "",
    val selectedType: String? = null,
    val selectedGen: Generation? = null,
    val error: String? = null
)

class MainViewModel(
    private val repo: PokemonRepository
): ViewModel() {

    private val _state = MutableStateFlow(MainUiState(loading = true))
    val state: StateFlow<MainUiState> = _state.asStateFlow()

    private var typeSetCache: Map<String, Set<String>> = emptyMap() // type -> set de nomes (lowercase)

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        runCatching {
            _state.update { it.copy(loading = true, error = null) }
            val list = repo.getPokemonList(100)
            _state.update { it.copy(loading = false, all = list, visible = list) }
        }.onFailure {
            _state.update { s -> s.copy(loading = false, error = it.message) }
        }
    }

    fun updateSearch(query: String) {
        _state.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun updateType(type: String?) {
        _state.update { it.copy(selectedType = type) }
        applyFilters()
    }

    fun updateGeneration(gen: Generation?) {
        _state.update { it.copy(selectedGen = gen) }
        applyFilters()
    }

    private fun applyFilters() = viewModelScope.launch {
        val current = _state.value
        var base = current.all

        // Filtro por geração (somente por ID localmente)
        current.selectedGen?.let { gen ->
            base = base.filter { repo.isFromGeneration(it.id, gen) }
        }

        // Filtro por tipo (usa endpoint /type/{type})
        val type = current.selectedType
        if (!type.isNullOrBlank()) {
            val set = typeSetCache[type] ?: repo.getPokemonNamesByType(type).also {
                typeSetCache = typeSetCache + (type to it)
            }
            base = base.filter { set.contains(it.name.lowercase()) }
        }

        // Busca por nome
        val q = current.searchQuery.trim().lowercase()
        if (q.isNotEmpty()) {
            base = base.filter { it.name.lowercase().contains(q) }
        }

        _state.update { it.copy(visible = base) }
    }
}
