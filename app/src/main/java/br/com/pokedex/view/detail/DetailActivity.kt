package br.com.pokedex.view.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import br.com.pokedex.databinding.ActivityDetailBinding
import br.com.pokedex.DetailVmFactory
import br.com.pokedex.R
import br.com.pokedex.view.detail.viewmodel.DetailViewModel
import br.com.pokedex.view.main.MainActivity
import br.com.pokedex.view.main.lifecycleScopeLaunchCollect

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val vm: DetailViewModel by viewModels { DetailVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val id = intent.getIntExtra("id", -1)
        val name = intent.getStringExtra("name")

        vm.load(if (id > 0) id.toString() else (name ?: ""))

        lifecycleScopeLaunchCollect(vm.state) { s ->
            binding.progressBar.visibility = if (s.loading) View.VISIBLE else View.GONE
            s.detail?.let { pokemon ->
                binding.name.text = pokemon.name
                binding.number.text = "#${pokemon.id}"
                binding.types.text = "Type(s): ${pokemon.types.joinToString(", ")}"
                binding.height.text = "Height: ${pokemon.height} m"
                binding.weight.text = "Weight: ${pokemon.weight} kg"
                binding.image.load(pokemon.imageUrl)

                val typeColor = when (pokemon.types.firstOrNull()?.lowercase()) {
                    "fire" -> R.color.pokemon_fire_bg
                    "water" -> R.color.pokemon_water_bg
                    "grass" -> R.color.pokemon_grass_bg
                    "electric" -> R.color.pokemon_electric_bg
                    "psychic" -> R.color.pokemon_psychic_bg
                    "ice" -> R.color.pokemon_ice_bg
                    "rock" -> R.color.pokemon_rock_bg
                    "ground" -> R.color.pokemon_ground_bg
                    "dark" -> R.color.pokemon_dark_bg
                    "fairy" -> R.color.pokemon_fairy_bg
                    "steel" -> R.color.pokemon_steel_bg
                    "ghost" -> R.color.pokemon_ghost_bg
                    else -> R.color.pokemon_default_bg
                }
                binding.root.setBackgroundResource(typeColor)
            }
            s.error?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
    }
}
