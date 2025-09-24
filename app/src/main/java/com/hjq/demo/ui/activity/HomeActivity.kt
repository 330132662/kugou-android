package com.hjq.demo.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.bb.kg.R
import com.google.android.material.navigation.NavigationView
import com.hjq.base.FragmentPagerAdapter
import com.hjq.demo.app.AppActivity
import com.hjq.demo.app.AppFragment
import com.hjq.demo.manager.ActivityManager
import com.hjq.demo.manager.Router
import com.hjq.demo.other.DoubleClickHelper
import com.hjq.demo.ui.adapter.NavigationAdapter
import com.hjq.demo.ui.fragment.HomeFragment
import com.hjq.demo.ui.fragment.MessageFragment
import com.hjq.demo.ui.fragment.MineFragment
import com.hjq.demo.ui.fragment.SingFragment
import com.hjq.demo.ui.fragment.VideoFragment
import timber.log.Timber
import kotlin.math.abs

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 首页界面
 */
@Route(path = Router.Core)
class HomeActivity : AppActivity(), NavigationAdapter.OnNavigationListener {

    private val drawerLayout: DrawerLayout? by lazy { findViewById(R.id.drawer_layout) }
    private val nav_view: NavigationView? by lazy { findViewById(R.id.nav_view) }
    private val nav_view_1: NavigationView? by lazy { findViewById(R.id.nav_view_1) }
    private val main_view: LinearLayoutCompat? by lazy { findViewById(R.id.main_view) }

    companion object {

        private const val INTENT_KEY_IN_FRAGMENT_INDEX: String = "fragmentIndex"
        private const val INTENT_KEY_IN_FRAGMENT_CLASS: String = "fragmentClass"

        @JvmOverloads
        fun start(
            context: Context,
            fragmentClass: Class<out AppFragment<*>?>? = MessageFragment::class.java
        ) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(INTENT_KEY_IN_FRAGMENT_CLASS, fragmentClass)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val viewPager: ViewPager? by lazy { findViewById(R.id.vp_home_pager) }
    private val navigationView: RecyclerView? by lazy { findViewById(R.id.rv_home_navigation) }
    private var navigationAdapter: NavigationAdapter? = null
    private var pagerAdapter: FragmentPagerAdapter<AppFragment<*>>? = null

    override fun getLayoutId(): Int {
        return R.layout.home_activity
    }

    override fun initView() {
        navigationAdapter = NavigationAdapter(this).apply {
            addItem(
                NavigationAdapter.MenuItem(
                    getString(R.string.home_nav_index),
                    ContextCompat.getDrawable(this@HomeActivity, R.drawable.home_home_selector)
                )
            )
            addItem(
                NavigationAdapter.MenuItem(
                    getString(R.string.home_nav_found),
                    ContextCompat.getDrawable(this@HomeActivity, R.drawable.home_home_selector)
                )
            )
            addItem(
                NavigationAdapter.MenuItem(
                    getString(R.string.home_nav_message),
                    ContextCompat.getDrawable(this@HomeActivity, R.drawable.home_home_selector)
                )
            )
            addItem(
                NavigationAdapter.MenuItem(
                    getString(R.string.home_nav_money),
                    ContextCompat.getDrawable(this@HomeActivity, R.drawable.home_home_selector)
                )
            )
            addItem(
                NavigationAdapter.MenuItem(
                    getString(R.string.home_nav_me),
                    ContextCompat.getDrawable(this@HomeActivity, R.drawable.home_home_selector)
                )
            )
            setOnNavigationListener(this@HomeActivity)
            navigationView?.adapter = this
        }
        main_view?.let {
            it?.setOnTouchListener { v, event ->
                handleTouchEvent(event);
                true;
            }
        }
        drawerLayout.let {
            it?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            it?.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    // 抽屉滑动时的回调
                    // 1. 主界面缩放效果 - 随着抽屉打开，主界面逐渐缩小
                    /*val scale = 1 - slideOffset * 0.2f  // 最大缩放到0.8倍
                    if (main_view != null) {
                        main_view?.scaleX = scale
                        main_view?.scaleY = scale

                        // 2. 主界面透明度变化 - 逐渐变暗
                        main_view?.alpha = 1 - slideOffset * 0.5f  // 最大透明度0.5

                        // 3. 主界面位移 - 向右移动一点，增强立体感
                        val translationX = drawerView.width * slideOffset * 0.3f
                        main_view?.translationX = translationX
                    }*/


                }

                override fun onDrawerOpened(drawerView: View) {
                    // 抽屉打开时的回调
                }

                override fun onDrawerClosed(drawerView: View) {
                    // 抽屉关闭时的回调
                }

                override fun onDrawerStateChanged(newState: Int) {
                    // 抽屉状态改变时的回调
                }
            })
        }
    }

    override fun initData() {
        viewPager.let {
            // 监听ViewPager触摸事件
            it?.setOnTouchListener { _, event ->
                handleTouchEvent(event)
                false // 返回false，不拦截事件，保证ViewPager正常滑动
            }
            it?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int, positionOffset: Float, positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    switchFragment(position)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
        }
        pagerAdapter = FragmentPagerAdapter<AppFragment<*>>(this).apply {
            addFragment(HomeFragment.newInstance())
            addFragment(VideoFragment.newInstance())
            addFragment(SingFragment.newInstance())
            addFragment(MessageFragment.newInstance())
            addFragment(MineFragment.newInstance())
            viewPager?.adapter = this

        }
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        pagerAdapter?.let {
            switchFragment(it.getFragmentIndex(getSerializable(INTENT_KEY_IN_FRAGMENT_CLASS)))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewPager?.let {
            // 保存当前 Fragment 索引位置
            outState.putInt(INTENT_KEY_IN_FRAGMENT_INDEX, it.currentItem)

        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // 恢复当前 Fragment 索引位置
        switchFragment(savedInstanceState.getInt(INTENT_KEY_IN_FRAGMENT_INDEX))
    }

    private fun switchFragment(fragmentIndex: Int) {
        Timber.d("switchFragment: $fragmentIndex")
        if (fragmentIndex == -1) {
            return
        }
        viewPager?.currentItem = fragmentIndex
        navigationAdapter?.setSelectedPosition(fragmentIndex)/*when (fragmentIndex) {
            0, 1, 2, 3 -> {

            }
        }*/
    }

    /**
     * [NavigationAdapter.OnNavigationListener]
     */
    override fun onNavigationItemSelected(position: Int): Boolean {
        viewPager?.currentItem = position

        return true;

        /*return when (position) {
            0, 1, 2, 3, 4 -> {
                viewPager?.currentItem = position
                true
            }

            else -> false
        }*/
    }


    override fun onBackPressed() {
        if (!DoubleClickHelper.isOnDoubleClick()) {
            toast(R.string.home_exit_hint)
            return
        }

        // 移动到上一个任务栈，避免侧滑引起的不良反应
        moveTaskToBack(false)
        postDelayed({
            // 进行内存优化，销毁掉所有的界面
            ActivityManager.getInstance().finishAllActivities()
        }, 300)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager?.adapter = null
        navigationView?.adapter = null
        navigationAdapter?.setOnNavigationListener(null)
    }

    // 滑动相关变量
    private var startX = 0f // 触摸起始X坐标
    private var currentX = 0f
    private var isDragging = false // 是否正在拖动
    private var velocityTracker: VelocityTracker? = null // 速度追踪器
    private var isDrawerMoving = false;
    private val touchSlop by lazy { ViewConfiguration.get(this).scaledTouchSlop }
    private val minFlingVelocity by lazy { ViewConfiguration.get(this).scaledMinimumFlingVelocity }
    private val drawerWidth by lazy { 280 } // 抽屉宽度（dp转px）

    /**
     * 记录上一个页面的位置  用于防止在最后一个fragment左滑时  显示了右侧抽屉
     */
    @Deprecated("实测不好用")
    private var lastCurrentPostion = -1;
    private val minVelocity by lazy {
        ViewConfiguration.get(this).scaledMinimumFlingVelocity // 最小滑动速度阈值
    }

    /**
     * 处理触摸事件，判断是否需要打开抽屉
     */
    private fun handleTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录起始位置
                startX = event.x
                isDragging = true
                // 初始化速度追踪器
                velocityTracker = VelocityTracker.obtain()
                velocityTracker?.addMovement(event)
            }

            MotionEvent.ACTION_MOVE -> {
                velocityTracker = VelocityTracker.obtain()
                velocityTracker?.addMovement(event)

                // 计算当前滑动距离
                /*val currentX = event.x
                val dx = currentX - startX*/ // 正值表示向右滑，负值向左滑
                isDragging = true

                val x = event.x
                val dx = x - startX // 滑动距离（正值向右）

                // 判断是否开始拖动（超过系统最小滑动距离）
                if (!isDragging && abs(dx) > touchSlop) {
                    isDragging = true
                    // 仅在第一个页面且向右滑时才处理
                    if (dx > 0 && viewPager?.currentItem == 0) {
                        isDrawerMoving = true
                    }
                }

                // 处理抽屉滑动
                if (isDrawerMoving) {
                    currentX = x
                    // 计算滑动比例（0-1）
                    val slideOffset = (x - startX) / drawerWidth.coerceAtLeast(1)
                    val clampedOffset = slideOffset.coerceIn(0f, 1f)

                    // 手动更新抽屉位置
//                    drawerLayout.se(clampedOffset)
//                    drawerLayout?.translationX = clampedOffset * drawerWidth
//                    applyStereoEffect(clampedOffset)
                    updateDrawerPosition(clampedOffset);
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val current = viewPager?.currentItem;
                if (current != null && velocityTracker != null) {
                    if (isDragging && (current == 0 || current == 4)) { // 仅在第一个页面处理
                        lastCurrentPostion = current;
                        velocityTracker?.apply {
                            computeCurrentVelocity(1000) // 计算速度（像素/秒）
                            val xVelocity = xVelocity // X方向速度（正值向右）
                            Timber.d("$current 上次 $lastCurrentPostion xVelocity: $xVelocity 速度 $minVelocity")
                            // 判断条件：向右滑动且速度达标
                            if (xVelocity > minVelocity && current == 0) {
                                // 打开抽屉
                                drawerLayout?.openDrawer(nav_view!!)
                            } else if (xVelocity < 0 && current == 4) {//&& lastCurrentPostion != current
                                drawerLayout?.openDrawer(nav_view_1!!)
                            }
                            recycle() // 回收速度追踪器
                        }
                    } else {
                        Timber.w("?????????? velocityTracker 是null ")
                    }
                }
                // 重置状态
                isDragging = false
                velocityTracker = null
            }
        }
        return false
    }

    // 触摸滑动时更新抽屉位置（完全基于官方 translationX API）
    private fun updateDrawerPosition(slideOffset: Float) {
        // 抽屉宽度：通过视图测量后获取（避免硬编码，适配不同屏幕）
        val drawerWidth = drawerLayout?.measuredWidth
        // 计算当前 translationX：-width（隐藏）→ 0（显示）
        val targetTranslationX = -drawerWidth!! + (drawerWidth * slideOffset)
        // 官方 API：设置抽屉的水平位移
        nav_view?.translationX = targetTranslationX
        // 同步更新立体感效果
        applyStereoEffect(slideOffset)
    }


    /**
     * 应用立体感效果（缩放、透明度、位移）
     */
    private fun applyStereoEffect(slideOffset: Float) {
        // 1. 主界面缩放（1.0 → 0.8）
        val scale = 1 - slideOffset * 0.2f
        main_view?.scaleX = scale
        main_view?.scaleY = scale

        // 2. 主界面透明度（1.0 → 0.5）
        main_view?.alpha = 1 - slideOffset * 0.5f

        // 3. 主界面位移（向右移动）
        main_view?.translationX = drawerLayout?.width!! * slideOffset * 0.3f

        // 4. 抽屉阴影增强（随打开比例增加阴影）
        nav_view?.elevation = 5f + slideOffset * 15f // 5dp → 20dp
    }
}