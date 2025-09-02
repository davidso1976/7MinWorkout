package com.example.a7minutesworkout.ui.fragment

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.bumptech.glide.Glide

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("app:isVisible")
    fun setIsVisible(view: View, isVisible: Boolean?) {
        view.isVisible = isVisible ?: false
    }

    @JvmStatic
    @BindingAdapter("app:srcDrawable")
    fun setDrawableSrc(view: ImageView, @DrawableRes src: Int) {
        view.setImageResource(src)
    }

    @JvmStatic
    @BindingAdapter("app:srcUrl")
    fun setImageSrcURL(view: ImageView, src: String?) {
        if(!src.isNullOrEmpty()) {
            Glide.with(view)
                .load(src)
                .into(view)
        }

    }

}