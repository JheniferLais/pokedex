package br.com.pokedex.view.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pokedex.data.PokemonRepository
import br.com.pokedex.data.local.model.PokemonDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUiState(
    val loading: Boolean = true,
    val detail: PokemonDetail? = null,
    val error: String? = null,
    val message: String? = null
)

class DetailViewModel(private val repo: PokemonRepository) : ViewModel() {
    private val _state = MutableStateFlow(DetailUiState())
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    fun load(idOrName: String) = viewModelScope.launch {
        runCatching {
            _state.update { it.copy(loading = true, error = null) }
            val d = repo.getPokemonDetail(idOrName)
            _state.update { it.copy(loading = false, detail = d) }
        }.onFailure {
            _state.update { it.copy(loading = false, error = it.message) }
        }
    }
}
