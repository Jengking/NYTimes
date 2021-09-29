package my.nytimes.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import my.nytimes.app.R
import my.nytimes.app.core.GlideApp
import my.nytimes.app.databinding.ArticleItemBinding
import my.nytimes.app.models.Docs
import my.nytimes.app.utils.DateHelper

class SearchAdapter: PagingDataAdapter<Docs, BaseViewHolder<Docs>>(DocsDiffUtils()) {

    class DocsDiffUtils: DiffUtil.ItemCallback<Docs>() {
        override fun areItemsTheSame(oldItem: Docs, newItem: Docs): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Docs, newItem: Docs): Boolean {
            return oldItem.abstract == newItem.abstract
        }
    }

    private val data: MutableList<Docs>

    init {
        data = ArrayList()
    }

    suspend fun addData(newList: List<Docs>) {
        data.addAll(newList)
        val paged = PagingData.from(data)
        submitData(paged)
    }

    suspend fun invalidated() {
        data.clear()
        val paged = PagingData.from(data)
        submitData(paged)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Docs>, position: Int) {
        val item = getItem(position)
        when (holder) {
            is BHolder -> {
                item?.let {
                    holder.bind(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Docs> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.article_item, parent, false)
        return BHolder(view)
    }

    class BHolder(view: View): BaseViewHolder<Docs>(view) {

        private val binding: ArticleItemBinding = ArticleItemBinding.bind(view)
        private val context = view.context

        override fun bind(item: Docs) {
            val multimedia = item.multimedia.firstOrNull{ it.width == 225 }
            if (multimedia != null) {
                val imgurl = "https://static01.nyt.com/${multimedia.url}"
                GlideApp.with(context).load(imgurl).into(binding.img)
            }
            binding.nameLabel.text = item.headline.main
            binding.popLabel.text = DateHelper.dateFormatChanger2(item.pub_date)
        }
    }
}