package com.hjq.demo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bb.kg.R
import com.hjq.base.CommonContext

class KugouAdapter(
    private val dataList: List<String>, private val baseWidth: Int,  // 基础宽度（dp）
    private val maxWidth: Int    // 最大宽度（dp）
) : RecyclerView.Adapter<KugouAdapter.ViewHolder>() {

    // 存储每个Item的当前宽度（px）
    private val itemWidths = mutableListOf<Int>()

    init {
        // 初始化宽度为基础宽度
        repeat(dataList.size) {
            itemWidths.add(dp2px(baseWidth))
//            itemWidths.add(baseWidth)
        }
    }

    // dp转px
    private fun dp2px(dp: Int): Int {
        val density = CommonContext.getContext().resources.displayMetrics.density
        return (dp * density).toInt()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container: ConstraintLayout = itemView.findViewById(R.id.item_container)
        val title: TextView = itemView.findViewById(R.id.tv_title)
    }

    override fun getItemViewType(position: Int): Int {
        /**
         *  索引0和第二行第一个 单独用大的布局
         */
        when (position) {
            0, 1 -> 0
            else -> 1
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_kugou, parent, false)
        if (viewType == 0) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_kugou0, parent, false)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 设置内容
        // 动态设置宽度
        val params = holder.container.layoutParams
//        params.width = itemWidths[position]
        if (getItemViewType(position) == 1) {
            params.height = 100;
        }
        holder.container.layoutParams = params
        holder.title.text = "${dataList[position]}-${itemWidths[position]}";
    }

    override fun getItemCount() = dataList.size

    // 更新Item宽度
    fun updateItemWidths(positions: List<Int>, widths: List<Int>) {
        positions.forEachIndexed { index, pos ->
            if (pos in itemWidths.indices) {
                itemWidths[pos] = widths[index]
                notifyItemChanged(pos)  // 局部刷新
            }
        }
    }


}