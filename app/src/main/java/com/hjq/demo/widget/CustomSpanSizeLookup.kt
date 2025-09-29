package com.hjq.demo.widget

import androidx.recyclerview.widget.GridLayoutManager

class CustomSpanSizeLookup(private val spanCount: Int) : GridLayoutManager.SpanSizeLookup() {

    override fun getSpanSize(position: Int): Int {
        return when (position) {
            0, 1 -> spanCount  // 前两个item占据整行（单行显示）
            else -> spanCount / 2  // 后面的item每个占据半行（两行显示）
        }
    }
}