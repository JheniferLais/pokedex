package br.com.pokedex.view.main

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.pokedex.databinding.ActivityMainBinding
import br.com.pokedex.data.local.model.Generation
import br.com.pokedex.MainVmFactory
import br.com.pokedex.view.detail.DetailActivity
import br.com.pokedex.view.main.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vm: MainViewModel by viewModels { MainVmFactory() }
    private val adapter = PokemonAdapter { item ->
        val i = Intent(this, DetailActivity::class.java)
        i.putExtra("id", item.id)
        i.putExtra("name", item.name)
        startActivity(i)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        // Busca
        binding.searchEdit.addTextChangedListener {
            vm.updateSearch(it?.toString().orEmpty())
        }

        // Spinner de tipo (string simples; vazio = sem filtro)
        val types = listOf(
            "","normal","fire","water","grass","electric","ice","fighting","poison","ground","flying",
            "psychic","bug","rock","ghost","dragon","dark","steel","fairy"
        )
        binding.typeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)
        binding.typeSpinner.setOnItemSelectedListenerCompat { pos ->
            val value = types[pos].ifEmpty { null }
            vm.updateType(value)
        }

        // Spinner de geração (vazio = sem filtro)
        val gens = listOf("","Gen I","Gen II","Gen III","Gen IV","Gen V","Gen VI","Gen VII","Gen VIII","Gen IX")
        binding.genSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, gens)
        binding.genSpinner.setOnItemSelectedListenerCompat { pos ->
            val value = gens[pos]
            val gen = Generation.values().firstOrNull { it.title == value }
            vm.updateGeneration(gen)
        }

        // Observa estado
        lifecycleScopeLaunchCollect(vm.state) { s ->
            binding.progressBar.isIndeterminate = s.loading
            binding.progressBar.visibility = if (s.loading) android.view.View.VISIBLE else android.view.View.GONE
            adapter.submitList(s.visible)
            if (s.error != null) {
                Toast.makeText(this, s.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
