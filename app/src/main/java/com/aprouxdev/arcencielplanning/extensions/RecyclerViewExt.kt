package com.aprouxdev.arcencielplanning.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.hasPreview(): Boolean {
    return getCurrentItem() > 0
}

fun RecyclerView.hasNext(dataSize: Int): Boolean {
    return this.adapter != null &&
            this.getCurrentItem() < dataSize - 1
}

fun RecyclerView.getCurrentItem(): Int {
    return (this.layoutManager as LinearLayoutManager)
        .findFirstVisibleItemPosition()
}