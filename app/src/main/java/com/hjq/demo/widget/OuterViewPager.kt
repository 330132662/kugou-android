package com.hjq.demo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

// OuterViewPagerA.kt
class OuterViewPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    private var startX = 0f
    private var startY = 0f
    private var isInterceptEnabled = true

    fun setInterceptEnabled(enabled: Boolean) {
        isInterceptEnabled = enabled
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!isInterceptEnabled) {
            return false
        }

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x
                startY = ev.y
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = abs(ev.x - startX)
                val deltaY = abs(ev.y - startY)

                // 如果是明显的水平滑动，自己处理
                if (deltaX > deltaY * 2) {
                    return super.onInterceptTouchEvent(ev)
                }
                // 否则不拦截，让子View处理
                return false
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}