package com.josycom.mayorjay.newsflash.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.josycom.mayorjay.newsflash.R
import com.josycom.mayorjay.newsflash.databinding.FragmentLoginBinding
import com.josycom.mayorjay.newsflash.overview.OverviewFragment
import com.josycom.mayorjay.newsflash.util.showToast
import com.josycom.mayorjay.newsflash.util.switchFragment
import java.util.concurrent.Executor

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val canAuthenticate = isBiometricsSupported()
        if (!canAuthenticate) {
            switchFragment(OverviewFragment(), null, false)
            return
        }

        initBiometricsAuth()
        setupListener()
    }

    private fun setupListener() {
        binding.btLogin.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun isBiometricsSupported() = BiometricManager.from(requireContext()).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS

    private fun initBiometricsAuth() {
        executor = ContextCompat.getMainExecutor(requireContext())

        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                showToast(getString(R.string.login_succeeded))
                switchFragment(OverviewFragment(), null, false)
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.fingerprint_login))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}