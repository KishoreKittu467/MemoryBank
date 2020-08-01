package com.kkapps.bubbleheadsview

interface OverlapRecyclerViewClickListener {
    fun onNormalItemClicked(adapterPosition: Int)

    fun onNumberedItemClick(adapterPosition: Int)
}