package com.kkapps.backpocket.extensions

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.kkapps.backpocket.models.Dp
import com.kkapps.backpocket.models.Task

fun ImageView.setDp(dp: Dp) {
    dp.let {
        when {
            !it.photo.isNullOrBlank() -> { Glide.with(context).load(it.photo).into(this) }
            it.icon != 0 -> { Glide.with(context).load(it.icon).into(this) }
            else -> { this.setBackgroundColor(ContextCompat.getColor(context, it.color)) }
        }
    }
}