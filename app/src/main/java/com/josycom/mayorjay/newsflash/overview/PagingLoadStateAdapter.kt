package com.josycom.mayorjay.newsflash.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.newsflash.databinding.PagingLoadStateFooterItemViewBinding

class PagingLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PagingLoadStateAdapter.PagingLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingLoadStateViewHolder {
        val binding = PagingLoadStateFooterItemViewBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return PagingLoadStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class PagingLoadStateViewHolder(
        private val binding: PagingLoadStateFooterItemViewBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                pbFooter.isVisible = loadState is LoadState.Loading
                btRetry.isVisible = loadState is LoadState.Error
                tvErrorMsg.isVisible = loadState is LoadState.Error
                tvErrorMsg.text = if (loadState is LoadState.Error) loadState.error.message else ""
            }
        }
    }
}