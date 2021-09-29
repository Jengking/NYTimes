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
import my.nytimes.app.models.Results
import my.nytimes.app.utils.DateHelper

class ArticleAdapter: PagingDataAdapter<Results, BaseViewHolder<Results>>(ResultsDiffUtils()) {

    class ResultsDiffUtils : DiffUtil.ItemCallback<Results>() {
        override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem.uri == newItem.uri
        }
    }

    private val data: MutableList<Results>

    init {
        data = ArrayList()
    }

    suspend fun addData(newList: List<Results>) {
        data.addAll(newList)
        val paged = PagingData.from(data)
        submitData(paged)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Results>, position: Int) {
        val item = getItem(position)
        when(holder) {
            is Aholder -> {
                item?.let {
                    holder.bind(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Results> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.article_item, parent, false)
        return Aholder(view)
    }

    class Aholder(view: View): BaseViewHolder<Results>(view) {

        private val binding: ArticleItemBinding = ArticleItemBinding.bind(view)
        private val context = view.context

        override fun bind(item: Results) {
            val imgurl = item.media[0].metadata[1].url
            GlideApp.with(context).load(imgurl).into(binding.img)

            binding.nameLabel.text = item.title
            binding.popLabel.text = DateHelper.dateFormatChanger(item.published_date)
        }
    }
}