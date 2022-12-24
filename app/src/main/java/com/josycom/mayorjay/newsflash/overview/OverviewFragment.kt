package com.josycom.mayorjay.newsflash.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.josycom.mayorjay.newsflash.R
import com.josycom.mayorjay.newsflash.databinding.FragmentOverviewBinding
import com.josycom.mayorjay.newsflash.login.LoginFragment
import com.josycom.mayorjay.newsflash.util.switchFragment
import kotlinx.coroutines.flow.collectLatest

class OverviewFragment : Fragment() {

    private lateinit var binding: FragmentOverviewBinding
    private val viewModel: OverviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOverviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.newsPagingFlow.collectLatest { data -> }
        }
    }

    private fun setupListener() {
//        binding.ivStatus.setOnClickListener {
//            viewModel.getCountries()
//        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                performLogout()
            }
        })
    }

    fun performLogout() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.warning))
            setMessage(getString(R.string.do_you_want_to_logout))
            setNegativeButton(getString(R.string.cancel) ) { _, _ -> }
            setPositiveButton(getString(R.string.logout)) { _, _ ->
                switchFragment(LoginFragment(), null, false)
            }
            show()
        }
    }

}