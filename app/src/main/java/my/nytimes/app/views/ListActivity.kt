package my.nytimes.app.views

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import my.nytimes.app.adapters.ArticleAdapter
import my.nytimes.app.core.Constants
import my.nytimes.app.databinding.ActivityListBinding
import my.nytimes.app.models.Articles
import my.nytimes.app.utils.Status
import my.nytimes.app.vm.ListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListActivity : BaseActivity() {

    companion object {
        const val PAGE_TITLE = "PAGE_TITLE"
    }

    private lateinit var binding: ActivityListBinding
    private val listViewModel: ListViewModel by viewModel()

    private var pageTitle = ""
    private val adapter = ArticleAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getExtras()
        setSupportActionBar(binding.toolbar)

        if (supportActionBar != null) {
            supportActionBar?.title = pageTitle
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setupComponent()
        fetchData()
        setupObserver()
    }

    private fun setupObserver() {
        listViewModel.data.observe(this, Observer {
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

    private fun passToAdapter(articles: Articles) {
        lifecycleScope.launch {
            val filtered = articles.results.filter { it.media.isNotEmpty() }
            adapter.addData(filtered)
        }
    }

    private fun fetchData() {
        when(pageTitle) {
            Constants.MOST_VIEWED_LBL -> listViewModel.fetchMostViewed()
            Constants.MOST_SHARED_LBL -> listViewModel.fetchMostShared()
            Constants.MOST_EMAILED_LBL -> listViewModel.fetchMostEmailed()
        }
    }

    private fun setupComponent() {
        val linear = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycler.layoutManager = linear
        binding.recycler.adapter = adapter
    }

    private fun getExtras() {
        pageTitle = intent.getStringExtra(PAGE_TITLE) ?: Constants.MOST_VIEWED_LBL
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}