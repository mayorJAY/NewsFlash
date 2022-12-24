package com.josycom.mayorjay.newsflash.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.newsflash.data.domain.Article
import com.josycom.mayorjay.newsflash.databinding.NewsItemLayoutBinding

class NewsAdapter(private val onArticleSelected: (article: Article) -> Unit) : PagingDataAdapter<Article, NewsAdapter.NewsViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemLayoutBinding.inflate(LayoutInflater.from(parent.context))
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

    class NewsViewHolder(binding: NewsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {}
    }
}