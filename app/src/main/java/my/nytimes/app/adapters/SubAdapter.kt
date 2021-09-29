package my.nytimes.app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import my.nytimes.app.R
import my.nytimes.app.core.GlideApp
import my.nytimes.app.databinding.ListingItemBinding
import my.nytimes.app.models.Results
import my.nytimes.app.utils.DateHelper

class SubAdapter: RecyclerView.Adapter<BaseViewHolder<Results>>() {

    private val data: MutableList<Results>

    init {
        data = ArrayList()
    }

    var screenWidth = 0.0

    @SuppressLint("NotifyDataSetChanged")
    fun addData(newList: List<Results>) {
        data.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Results>, position: Int) {
        val item = data[position]
        when (holder) {
            is ListingHolder -> {
                holder.bind(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Results> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.listing_item, parent, false)
        return ListingHolder(view, screenWidth)
    }

    class ListingHolder(val view: View, val screenWidth: Double): BaseViewHolder<Results>(view) {

        private val binding: ListingItemBinding = ListingItemBinding.bind(view)
        private val context = view.context

        override fun bind(item: Results) {
            val lparams = view.layoutParams
            lparams.width = screenWidth.toInt()
            view.layoutParams = lparams

            val imgUrl = item.media[0].metadata[1].url
            GlideApp.with(context).load(imgUrl).into(binding.img)

            binding.nameLabel.text = item.title
            binding.popLabel.text = DateHelper.dateFormatChanger(item.published_date)
        }
    }

    override fun getItemCount(): Int = data.size
}