package br.com.pokedex.view.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import br.com.pokedex.databinding.ActivityDetailBinding
import br.com.pokedex.DetailVmFactory
import br.com.pokedex.view.detail.viewmodel.DetailViewModel
import br.com.pokedex.view.main.lifecycleScopeLaunchCollect

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val vm: DetailViewModel by viewModels { DetailVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("id", -1)
        val name = intent.getStringExtra("name")

        vm.load(if (id > 0) id.toString() else (name ?: ""))

        lifecycleScopeLaunchCollect(vm.state) { s ->
            binding.progressBar.visibility = if (s.loading) View.VISIBLE else View.GONE
            s.detail?.let { d ->
                binding.name.text = "${d.name} (#${d.id})"
                binding.image.load(d.imageUrl) { crossfade(true) }
                binding.types.text = d.types.joinToString(" / ")
                binding.hw.text = "Altura: ${d.height} dm • Peso: ${d.weight} hg"
                val hp = d.stats["hp"] ?: 0
                val atk = d.stats["attack"] ?: 0
                val def = d.stats["defense"] ?: 0
                binding.stats.text = "HP: $hp • Attack: $atk • Defense: $def"
            }
            s.error?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
    }
}
