package com.eliving.tv.ext

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.eliving.tv.AppContext
import com.eliving.tv.R

@BindingAdapter("url")
fun loadImage(imageView: ImageView, url: String) {
    Glide.with(AppContext).load(url)
        .placeholder(R.mipmap.ic_launcher)
        .into(imageView)
}