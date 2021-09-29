package my.nytimes.app.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import my.nytimes.app.R
import my.nytimes.app.core.Constants
import my.nytimes.app.databinding.MainListItemBinding
import my.nytimes.app.models.Articles
import my.nytimes.app.views.ListActivity
import kotlin.coroutines.suspendCoroutine

class MainAdapter: PagingDataAdapter<Articles, BaseViewHolder<Articles>>(MainDiffUtil()) {

    class MainDiffUtil: DiffUtil.ItemCallback<Articles>() {
        override fun areItemsTheSame(oldItem: Articles, newItem: Articles): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Articles, newItem: Articles): Boolean {
            return oldItem.num_results == newItem.num_results
        }
    }

    private val data: MutableList<Articles>

    init {
        data = ArrayList()
    }

    var screenWidth: Int = 0

    suspend fun addData(newList: List<Articles>) {
        data.addAll(newList)
        val paged = PagingData.from(data)
        submitData(paged)
    }

    companion object {
        const val MOST_VIEWED = 0
        const val MOST_SHARED = 1
        const val MOST_EMAILED = 2
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Articles>, position: Int) {
        val item = getItem(position)
        when(holder) {
            is MostViewHolder -> {
                item?.let {
                    holder.bind(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Articles> {
        val context = parent.context
        return when(viewType) {
            MOST_VIEWED -> {
                val view = LayoutInflater.from(context).inflate(R.layout.main_list_item, parent, false)
                MostViewHolder(view, Constants.MOST_VIEWED_LBL, screenWidth)
            }
            MOST_SHARED -> {
                val view = LayoutInflater.from(context).inflate(R.layout.main_list_item, parent, false)
                MostViewHolder(view, Constants.MOST_SHARED_LBL, screenWidth)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.main_list_item, parent, false)
                MostViewHolder(view, Constants.MOST_EMAILED_LBL, screenWidth)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> MOST_VIEWED
            1 -> MOST_SHARED
            else -> MOST_EMAILED
        }
    }

    class MostViewHolder(view: View, private val title: String, private val screenWidth: Int): BaseViewHolder<Articles>(view) {

        private val binding: MainListItemBinding = MainListItemBinding.bind(view)
        private val context = view.context

        private val subAdapter = SubAdapter()

        override fun bind(item: Articles) {
            subAdapter.screenWidth = screenWidth / 2.5
            binding.headerTitle.text = title
            val listing = item.results.filter { it.media.isNotEmpty() }
            val horizontalyt = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.container.layoutManager = horizontalyt
            binding.container.adapter = subAdapter

            subAdapter.addData(listing)

            binding.morebtn.setOnClickListener { v ->
                val intent = Intent(context, ListActivity::class.java)
                intent.putExtra(ListActivity.PAGE_TITLE, title)
                context.startActivity(intent)
            }
        }
    }
}