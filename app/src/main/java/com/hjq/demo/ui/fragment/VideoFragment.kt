package com.hjq.demo.ui.fragment

import android.view.View
import com.bb.kg.R
import com.hjq.demo.app.AppFragment
import com.hjq.demo.ui.activity.CopyActivity

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 可进行拷贝的副本
 */
class VideoFragment : AppFragment<CopyActivity>() {

    companion object {

        fun newInstance(): VideoFragment {
            return VideoFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.find_fragment
    }

    override fun initView() {}

    override fun initData() {}

}