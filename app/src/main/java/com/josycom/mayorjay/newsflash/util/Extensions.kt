package com.josycom.mayorjay.newsflash.util

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.josycom.mayorjay.newsflash.R

fun Fragment.switchFragment(destination: Fragment, bundle: Bundle?, addToBackStack: Boolean) {
    this.parentFragmentManager.beginTransaction().apply {
        bundle?.let { destination.arguments = it }
        replace(R.id.mainFragment, destination)
        if (addToBackStack) addToBackStack(null)
        commit()
    }
}

fun Fragment.showToast(message: String) = Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show()