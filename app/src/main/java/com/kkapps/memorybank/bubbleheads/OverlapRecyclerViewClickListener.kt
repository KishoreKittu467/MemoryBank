package com.kkapps.memorybank.bubbleheads

interface OverlapRecyclerViewClickListener {
    fun onNormalItemClicked(adapterPosition: Int)

    fun onNumberedItemClick(adapterPosition: Int)
}