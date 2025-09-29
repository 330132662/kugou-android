package com.hjq.demo.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlin.math.abs

// ClickAwareRecyclerView.kt 自定义 RecyclerView 识别点击目标
class ClickAwareRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var startX = 0f
    private var startY = 0f
    private var isClickOnScrollable = false

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = e.x
                startY = e.y
                isClickOnScrollable = isPointOnScrollableArea(e.x, e.y)

                if (isClickOnScrollable) {
                    // 点击在可滚动区域，自己处理
                    parent.requestDisallowInterceptTouchEvent(true)
                } else {
                    // 点击在非滚动区域，交给父View处理
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = abs(e.x - startX)
                val deltaY = abs(e.y - startY)

                // 如果是水平滑动且点击在可滚动区域，自己处理
                if (deltaX > deltaY && isClickOnScrollable) {
                    parent.requestDisallowInterceptTouchEvent(true)
                } else {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return super.onInterceptTouchEvent(e)
    }

    /**
     * 判断点击位置是否在可滚动的内容区域
     */
    private fun isPointOnScrollableArea(x: Float, y: Float): Boolean {
        // 获取可见的第一个和最后一个item位置
        val layoutManager = layoutManager as? LinearLayoutManager ?: return false

        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

        if (firstVisiblePosition == RecyclerView.NO_POSITION) return false

        // 检查点击位置是否在某个item上
        for (i in firstVisiblePosition..lastVisiblePosition) {
            val childView = layoutManager.findViewByPosition(i)
            if (childView != null) {
                val rect = Rect()
                childView.getGlobalVisibleRect(rect)

                // 将点击坐标转换为全局坐标
                val location = IntArray(2)
                getLocationOnScreen(location)
                val globalX = x + location[0]
                val globalY = y + location[1]

                if (rect.contains(globalX.toInt(), globalY.toInt())) {
                    return true
                }
            }
        }

        return false
    }
}