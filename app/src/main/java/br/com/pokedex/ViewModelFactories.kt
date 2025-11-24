package br.com.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.pokedex.data.PokemonRepository
import br.com.pokedex.data.api.RetrofitModule
import br.com.pokedex.view.detail.viewmodel.DetailViewModel
import br.com.pokedex.view.main.viewmodel.MainViewModel

@Suppress("UNCHECKED_CAST")
class MainVmFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = PokemonRepository(RetrofitModule.api)
        return MainViewModel(repo) as T
    }
}

@Suppress("UNCHECKED_CAST")
class DetailVmFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = PokemonRepository(RetrofitModule.api)
        return DetailViewModel(repo) as T
    }
}
