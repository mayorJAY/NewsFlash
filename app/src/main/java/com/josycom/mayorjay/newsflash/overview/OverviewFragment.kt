package com.josycom.mayorjay.newsflash.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.josycom.mayorjay.newsflash.R
import com.josycom.mayorjay.newsflash.data.domain.Article
import com.josycom.mayorjay.newsflash.databinding.FragmentOverviewBinding
import com.josycom.mayorjay.newsflash.login.LoginFragment
import com.josycom.mayorjay.newsflash.util.switchFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        fetchAndDisplayNewsHeadlines()
        setupListener()
    }

    private fun fetchAndDisplayNewsHeadlines() {
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter =
                newsAdapter.withLoadStateFooter(PagingLoadStateAdapter { newsAdapter.retry() })
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
                viewModel.newsPagingFlow.collectLatest { data -> newsAdapter.submitData(data) }
            }
        }
    }

    private fun setupListener() {
        binding.ivStatus.setOnClickListener {
            newsAdapter.retry()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    performLogout()
                }
            })
    }

    private fun onArticleSelected(article: Article) {}

    fun performLogout() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.warning))
            setMessage(getString(R.string.do_you_want_to_logout))
            setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            setPositiveButton(getString(R.string.logout)) { _, _ ->
                switchFragment(LoginFragment(), null, false)
            }
            show()
        }
    }

}