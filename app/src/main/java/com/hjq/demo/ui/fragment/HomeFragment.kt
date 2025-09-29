package com.hjq.demo.ui.fragment

import androidx.viewpager.widget.ViewPager
import com.bb.kg.R
import com.google.android.material.tabs.TabLayout
import com.hjq.demo.app.AppFragment
import com.hjq.demo.ui.activity.HomeActivity
import com.hjq.demo.ui.adapter.ViewPagerAdapter
import com.hjq.widget.layout.NestedViewPager

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 可进行拷贝的副本
 */
class HomeFragment : AppFragment<HomeActivity>() {
    private val tabLayout: TabLayout by lazy { requireView().findViewById(R.id.tab_layout) }
    private val vp_home_pager: NestedViewPager by lazy { requireView().findViewById(R.id.vp_home_pager) }

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.home_fragment
    }

    override fun initView() {
        setupTabLayout();
        setupViewPager();
    }

    override fun initData() {}
    private lateinit var adapter: ViewPagerAdapter

    private fun setupViewPager() {
        adapter = ViewPagerAdapter(requireFragmentManager(), lifecycle)
        vp_home_pager.adapter = adapter
        // 设置预加载页面数（可选）
        vp_home_pager.offscreenPageLimit = 2
    }

    private fun setupTabLayout() {
        adapter = ViewPagerAdapter(requireFragmentManager(), lifecycle);
        vp_home_pager.adapter = adapter;
        tabLayout.setupWithViewPager(vp_home_pager)
        // 添加标签选择监听器
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // 标签被选中时的操作
                tab?.let {
                    vp_home_pager.currentItem = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 标签取消选中时的操作
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 标签重新选中时的操作（点击已选中的标签）
            }
        })
    }
}