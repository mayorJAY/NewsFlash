package com.josycom.mayorjay.newsflash.login

import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val isSupported = isBiometricSupported()
        if (isSupported == null) {
            switchFragment(OverviewFragment(), null, false)
            return
        }

        if (!isSupported) return

        initBiometricsAuth()

        binding.btLogin.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

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
            .setTitle(getString(R.string.biometric_login))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun isBiometricSupported(): Boolean? {
        val keyguardManager = requireContext().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardSecure) {
            showToast(getString(R.string.fingerprint_not_enabled))
            return false
        }

//        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
//            showToast("Fingerprint Permission not granted")
//            return false
//        }

        return if (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT))
            true else null
    }

}