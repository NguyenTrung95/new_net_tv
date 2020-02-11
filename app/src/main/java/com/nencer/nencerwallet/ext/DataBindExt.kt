package com.nencer.nencerwallet.ext

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.nencer.nencerwallet.AppContext
import com.nencer.nencerwallet.R

@BindingAdapter("url")
fun loadImage(imageView: ImageView, url: String) {
    Glide.with(AppContext).load(url)
        .placeholder(R.mipmap.ic_launcher)
        .into(imageView)
}