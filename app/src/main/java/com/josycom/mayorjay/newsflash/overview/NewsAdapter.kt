package com.josycom.mayorjay.newsflash.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.newsflash.data.domain.Article
import com.josycom.mayorjay.newsflash.databinding.NewsItemViewBinding
import com.josycom.mayorjay.newsflash.util.displayImage

class NewsAdapter(private val onArticleSelected: (article: Article) -> Unit) : PagingDataAdapter<Article, NewsAdapter.NewsViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemViewBinding.inflate(LayoutInflater.from(parent.context))
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        getItem(position)?.let { article ->
            holder.bind(article)
            holder.itemView.setOnClickListener { onArticleSelected.invoke(article) }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) = oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Article, newItem: Article) = oldItem == newItem
    }

    class NewsViewHolder(private val binding: NewsItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.apply {
                ivArticleImage.displayImage(article.imageUrl)
                tvArticleTitle.text = article.title
                tvArticleDate.text = article.date
            }
        }
    }
}