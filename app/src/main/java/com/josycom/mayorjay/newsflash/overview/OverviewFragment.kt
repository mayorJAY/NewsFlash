package com.josycom.mayorjay.newsflash.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.josycom.mayorjay.newsflash.BuildConfig
import com.josycom.mayorjay.newsflash.R
import com.josycom.mayorjay.newsflash.data.domain.Article
import com.josycom.mayorjay.newsflash.databinding.FragmentOverviewBinding
import com.josycom.mayorjay.newsflash.details.DetailsFragment
import com.josycom.mayorjay.newsflash.util.Constants
import com.josycom.mayorjay.newsflash.util.getModifiedNewsSource
import com.josycom.mayorjay.newsflash.util.switchFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OverviewFragment : Fragment() {

    private lateinit var binding: FragmentOverviewBinding
    private val viewModel: OverviewViewModel by viewModels()
    private val newsAdapter: NewsAdapter by lazy { NewsAdapter { onArticleSelected(it) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOverviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        fetchAndDisplayNewsHeadlines()
        setupListener()
    }

    private fun initView() {
        binding.tvHeader.text = BuildConfig.SOURCE_NAME.getModifiedNewsSource(getString(R.string.news))
    }

    private fun fetchAndDisplayNewsHeadlines() {
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = newsAdapter.withLoadStateFooter(PagingLoadStateAdapter { newsAdapter.retry() })
        }

        viewLifecycleOwner.lifecycleScope.launch {
            newsAdapter.loadStateFlow.collect {
                val loadState = it.source.refresh
                if (loadState is LoadState.Loading) {
                    binding.tvStatus.isVisible = false
                    binding.ivStatus.isVisible = true
                    binding.ivStatus.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.loading_animation,
                            null
                        )
                    )
                }

                if (loadState is LoadState.Error) {
                    binding.tvStatus.isVisible = true
                    binding.tvStatus.text = getString(R.string.network_error_message, loadState.error.message)
                    binding.ivStatus.isVisible = true
                    binding.ivStatus.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_connection_error,
                            null
                        )
                    )
                }

                if (loadState !is LoadState.Loading && loadState !is LoadState.Error) {
                    binding.ivStatus.isVisible = false
                    binding.tvStatus.isVisible = false
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getNewsPagingFlow().collectLatest { data ->
                    newsAdapter.submitData(data)
                }
            }
        }
    }

    private fun setupListener() {
        binding.ivStatus.setOnClickListener {
            newsAdapter.retry()
        }
    }

    private fun onArticleSelected(article: Article) {
        val arg = Bundle().apply { putSerializable(Constants.ARTICLE_KEY, article) }
        switchFragment(DetailsFragment(), arg, true)
    }
}