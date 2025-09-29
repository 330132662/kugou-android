package com.hjq.demo.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.lifecycle.Lifecycle
import com.hjq.base.FragmentPagerAdapter
import com.hjq.demo.ui.fragment.HomeFragment

// ViewPagerAdapter.kt
class ViewPagerAdapter(
    fm: FragmentManager, private val lifecycle: Lifecycle
) : androidx.fragment.app.FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = listOf(
        HomeFragment.newInstance(),
        HomeFragment.newInstance(),
        HomeFragment.newInstance(),
        HomeFragment.newInstance(),

        )

    private val tabTitles = listOf("首页", "发现", "音乐", "我的")
    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }
}