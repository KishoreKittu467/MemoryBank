package com.kkapps.memorybank.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kkapps.memorybank.R
import com.kkapps.memorybank.models.Image

fun ImageView.setDisplayPic(image: Image, defaultIconOrColor: Int = 0) {
    image.let {
        Glide.with(context).load(
            when {
                !it.photo.isNullOrBlank() -> it.photo
                defaultIconOrColor != 0 -> defaultIconOrColor
                it.color != 0 -> it.color
                it.icon != 0 -> it.icon
                else -> R.color.colorAccent
            }
        ).into(this)
    }
}