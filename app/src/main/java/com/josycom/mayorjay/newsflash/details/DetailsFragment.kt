package com.josycom.mayorjay.newsflash.details

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.viewModels
import com.josycom.mayorjay.newsflash.data.domain.Article
import com.josycom.mayorjay.newsflash.databinding.FragmentDetailsBinding
import com.josycom.mayorjay.newsflash.util.Constants
import com.josycom.mayorjay.newsflash.util.displayImage

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setupListener()
    }

    private fun initView() {
        val article = arguments?.getSerializable(Constants.ARTICLE_KEY) as Article?
        viewModel.setArticle(article)
        binding.apply {
            article?.let {
                ivArticleImage.displayImage(it.imageUrl)
                tvArticleTitle.text = it.title
                tvArticleDate.text = it.date
                tvArticleDesc.text = it.description
                tvArticleContent.text = it.content
            }
        }
    }

    private fun setupListener() {
        binding.btReadMore.setOnClickListener {
            viewModel.getArticle().value?.let { launchArticleInBrowser(it.url) }
        }
    }

    private fun launchArticleInBrowser(url: String) {
        CustomTabsIntent.Builder().build().apply {
            launchUrl(requireContext(), Uri.parse(url))
        }
    }
}