package my.nytimes.app.views

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import my.nytimes.app.adapters.SearchAdapter
import my.nytimes.app.databinding.ActivitySearchBinding
import my.nytimes.app.models.Searches
import my.nytimes.app.utils.Status
import my.nytimes.app.vm.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity() {

    private lateinit var binding:ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModel()

    private val adapter = SearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)

        if (supportActionBar != null) {
            supportActionBar?.title = null
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding.progressBar.visibility = View.GONE

        setupComponent()
        setupObserver()
        setEventListeners()
    }

    private fun setEventListeners() {
        binding.searchSV.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchSV.clearFocus()
                query?.let {
                    reset()
                    searchViewModel.fetchSearch(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupObserver() {
        searchViewModel.data.observe(this, Observer {
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
                    promptError(true)
                }
            }
        })
    }

    private fun reset() {
        lifecycleScope.launch {
            adapter.invalidated()
        }
    }

    private fun passToAdapter(searches: Searches) {
        lifecycleScope.launch {
            val listdata = searches.response.docs
            adapter.addData(listdata)
        }
    }

    private fun setupComponent() {
        val linear = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycler.layoutManager = linear
        binding.recycler.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}