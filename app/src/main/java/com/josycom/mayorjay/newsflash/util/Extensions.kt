package com.josycom.mayorjay.newsflash.util

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.josycom.mayorjay.newsflash.R
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Fragment.switchFragment(destination: Fragment, bundle: Bundle?, addToBackStack: Boolean) {
    this.parentFragmentManager.beginTransaction().apply {
        bundle?.let { destination.arguments = it }
        replace(R.id.main_fragment, destination)
        if (addToBackStack) addToBackStack(null)
        commit()
    }
}

fun Fragment.showToast(message: String) = Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show()

fun ImageView.displayImage(imageUrl: String) {
    Glide.with(this.context)
        .load(imageUrl)
        .placeholder(android.R.drawable.progress_indeterminate_horizontal)
        .fallback(android.R.drawable.progress_indeterminate_horizontal)
        .error(android.R.drawable.stat_notify_error)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun String.getModifiedNewsSource(searchStr: String): String {
    if (this.contains(searchStr, true)) return this
    return "$this $searchStr"
}

fun String.getFormattedDate(): String {
    var fromFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", Locale.getDefault())
    val toFormat = SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.getDefault())
    return try {
        toFormat.format(fromFormat.parse(this) ?: Date())
    } catch (e: ParseException) {
        Timber.e(e)
        try {
            fromFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            toFormat.format(fromFormat.parse(this) ?: Date())
        } catch (e: ParseException) {
            Timber.e(e)
            try {
                fromFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                toFormat.format(fromFormat.parse(this) ?: Date())
            } catch (e: ParseException) {
                Timber.e(e)
                this
            }
        }
    }
}