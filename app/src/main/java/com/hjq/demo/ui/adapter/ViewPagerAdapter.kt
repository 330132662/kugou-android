package com.hjq.demo.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.hjq.demo.ui.fragment.home.FoundFragment
import com.hjq.demo.ui.fragment.home.RecommendFragment
import com.hjq.demo.ui.fragment.home.TingshuFragment
import com.hjq.demo.ui.fragment.home.YuekuFragment

// ViewPagerAdapter.kt
class ViewPagerAdapter(
    fm: FragmentManager, private val lifecycle: Lifecycle
) : androidx.fragment.app.FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = listOf(
        /*RecommendFragment(),
        RecommendFragment(),
        RecommendFragment(),
        RecommendFragment(),*/
        RecommendFragment.newInstance(),
        YuekuFragment.newInstance(),
        TingshuFragment.newInstance(),
        FoundFragment.newInstance(),

        )

    private val tabTitles = listOf("推荐", "乐库", "听书", "发现")
    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }
}