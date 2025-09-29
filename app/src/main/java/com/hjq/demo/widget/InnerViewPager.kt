package com.hjq.demo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

// InnerViewPagerB.kt
class InnerViewPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    private var startX = 0f
    private var startY = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x
                startY = ev.y
                parent.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = abs(ev.x - startX)
                val deltaY = abs(ev.y - startY)

                // 如果是明显的水平滑动，自己处理
                if (deltaX > deltaY * 2) {
                    parent.requestDisallowInterceptTouchEvent(true)
                } else {
                    // 垂直滑动或不确定，交给外层处理
                    parent.requestDisallowInterceptTouchEvent(false)
                    return false
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}