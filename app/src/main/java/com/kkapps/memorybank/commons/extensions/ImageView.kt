package com.kkapps.memorybank.commons.extensions

import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kkapps.memorybank.R
import com.kkapps.memorybank.home.models.Image

fun ImageView.setFillWithStroke(fillColor: Int, backgroundColor: Int) {
    val strokeColor = backgroundColor.getContrastColor()
    GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(fillColor)
        setStroke(2, strokeColor)
        setBackgroundDrawable(this)
    }
}

fun ImageView.applyColorFilter(color: Int) = setColorFilter(color, PorterDuff.Mode.SRC_IN)

fun ImageView.setDisplayPic(image: Image, defaultIconOrColor: Int = 0) {
    image.let {
        Glide.with(context).load(
            when {
                !it.photo.isNullOrBlank() -> it.photo
                defaultIconOrColor != 0 -> defaultIconOrColor
                it.color != 0 -> it.color
                it.icon != 0 -> it.icon
                else -> R.color.color_accent
            }
        ).into(this)
    }
}