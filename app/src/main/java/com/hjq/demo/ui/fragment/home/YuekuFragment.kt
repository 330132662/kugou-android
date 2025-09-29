package com.hjq.demo.ui.fragment.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bb.kg.R
import com.hjq.demo.app.AppFragment
import com.hjq.demo.ui.activity.HomeActivity
import com.hjq.demo.ui.adapter.KugouAdapter
import com.hjq.demo.widget.ClickAwareRecyclerView

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   :  推荐Fragment
 */
class YuekuFragment : AppFragment<HomeActivity>() {
    private val recyclerView: ClickAwareRecyclerView by lazy { requireView().findViewById(R.id.recyclerView) }

    //    private lateinit var adapter: HorizontalAdapter
    private lateinit var adapter: KugouAdapter

    companion object {

        fun newInstance(): YuekuFragment {
            return YuekuFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.recommend_fragment
    }

    override fun initView() {
//        setupRecyclerView();
        initRecyclerView();
    }

    override fun initData() {}

    // 配置参数
    private val baseWidthDp = 120  // 基础宽度（离中心最远时的宽度）
    private val maxWidthDp = 180   // 最大宽度（中心位置的宽度）
    private val scrollRangeDp = 200 // 影响宽度变化的滑动范围（px）
    var scrollRangePx = 0;
    private fun initRecyclerView() {
        // 模拟数据
        val dataList = mutableListOf<String>()
        repeat(20) { dataList.add("推荐歌单 $it") }

        // 初始化适配器
        adapter = KugouAdapter(dataList, baseWidthDp, maxWidthDp)

        recyclerView.adapter = adapter

//        recyclerView.layoutManager =     LinearLayoutManager(getAttachActivity(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager =
            GridLayoutManager(getAttachActivity(), 2, GridLayoutManager.HORIZONTAL, true)

        // 计算滑动影响范围（dp转px）
        val density = resources.displayMetrics.density
        scrollRangePx = (scrollRangeDp * density).toInt()

        // 监听滑动事件
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
//                updateItemWidths()
            }
        })

        // 首次加载完成后初始化宽度
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                updateItemWidths()
            }
        })
    }

    // 更新所有可见Item的宽度
    private fun updateItemWidths() {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisiblePos = layoutManager.findFirstVisibleItemPosition()
        val lastVisiblePos = layoutManager.findLastVisibleItemPosition()

        if (firstVisiblePos == -1 || lastVisiblePos == -1) return

        val centerX = recyclerView.width / 2  // 屏幕中心X坐标（px）
        val positions = mutableListOf<Int>()
        val widths = mutableListOf<Int>()

        // 计算每个可见Item的宽度
        for (pos in firstVisiblePos..lastVisiblePos) {
            val view = layoutManager.findViewByPosition(pos) ?: continue
            val itemCenterX = view.left + view.width / 2  // Item中心X坐标
            val distance = Math.abs(itemCenterX - centerX)  // 与屏幕中心的距离

            // 计算宽度比例（距离越近，比例越大）
            val ratio = 1 - (distance.toFloat() / scrollRangePx).coerceAtMost(1f)
            // 根据比例计算宽度（基础宽度 + 比例*(最大-基础)）
            val widthPx =
                (baseWidthDp * resources.displayMetrics.density + ratio * (maxWidthDp - baseWidthDp) * resources.displayMetrics.density).toInt()

            positions.add(pos)
            widths.add(widthPx)
        }

        // 通知适配器更新宽度
//        adapter.updateItemWidths(positions, widths)
    }

    /*private fun setupRecyclerView() {


        // 设置水平布局
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        adapter = HorizontalAdapter(createTestData())
        recyclerView.adapter = adapter

        // 添加Item点击监听
        adapter.setOnItemClickListener { position, item ->
            // 处理item点击
            Toast.makeText(requireContext(), "点击了: $item", Toast.LENGTH_SHORT).show()
        }
    }*/

    private fun createTestData(): List<String> {
        return listOf(
            "Item 1",
            "Item 2",
            "Item 3",
            "Item 4",
            "Item 5",
            "Item 6",
            "Item 7",
            "Item 8",
            "Item 9",
            "Item 10"
        )
    }

    // HorizontalAdapter.kt
    class HorizontalAdapter(private val data: List<String>) :
        RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {

        private var itemClickListener: ((Int, String) -> Unit)? = null

        fun setOnItemClickListener(listener: (Int, String) -> Unit) {
            itemClickListener = listener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_horizontal, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(data[position])
            holder.itemView.setOnClickListener {
                itemClickListener?.invoke(position, data[position])
            }
        }

        override fun getItemCount(): Int = data.size

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val textView: TextView = itemView.findViewById(R.id.textView)

            fun bind(item: String) {
                textView.text = item
            }
        }
    }
}