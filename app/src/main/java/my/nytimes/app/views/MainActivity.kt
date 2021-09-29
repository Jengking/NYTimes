package my.nytimes.app.views

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import my.nytimes.app.R
import my.nytimes.app.adapters.MainAdapter
import my.nytimes.app.databinding.ActivityMainBinding
import my.nytimes.app.models.Articles
import my.nytimes.app.utils.Status
import my.nytimes.app.vm.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()

    private val adapter = MainAdapter()
    private var metrics = DisplayMetrics()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        windowManager.defaultDisplay.getRealMetrics(metrics)
        setSupportActionBar(binding.toolbar)

        if (supportActionBar != null) {
            supportActionBar?.title = "New York Times"
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setHomeButtonEnabled(false)
        }
        adapter.screenWidth = metrics.widthPixels
        setupComponent()
        setupObserver()
    }

    private fun setupObserver() {
        mainViewModel.data.observe(this, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    it.data?.let { listing ->
                        passToAdapter(listing)
                    }
                }
                Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    promptError(false)
                }
            }
        })
    }

    private fun passToAdapter(list: List<Articles>) {
        lifecycleScope.launch {
            adapter.addData(list)
        }
    }

    private fun setupComponent() {
        val linear = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycler.layoutManager = linear
        binding.recycler.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_search -> {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}