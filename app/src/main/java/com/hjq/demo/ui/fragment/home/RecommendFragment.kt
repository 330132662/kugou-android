package com.hjq.demo.ui.fragment.home

import com.bb.kg.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hjq.demo.app.AppFragment
import com.hjq.demo.ui.activity.HomeActivity
import com.hjq.demo.ui.adapter.ViewPagerAdapter

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 可进行拷贝的副本
 */
class RecommendFragment : AppFragment<HomeActivity>() {
    private val tabLayout: TabLayout by lazy { requireView().findViewById(R.id.tab_layout) }

    companion object {

        fun newInstance(): RecommendFragment {
            return RecommendFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.home_fragment
    }

    override fun initView() {

    }

    override fun initData() {}
    private lateinit var adapter: ViewPagerAdapter

}