package com.hjq.demo.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 首页界面
 */
@Route(path = Router.Core)
class HomeActivity : AppActivity(), NavigationAdapter.OnNavigationListener {

    private val drawerLayout: DrawerLayout? by lazy { findViewById(R.id.drawer_layout) }

    //    private val nav_view: NavigationView? by lazy { findViewById(R.id.nav_view) }
    private val nav_view: LinearLayoutCompat? by lazy { findViewById(R.id.nav_view) }
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
        nav_view?.let {
            it.setOnTouchListener { v, event ->
                Timber.d("HomeActivity touch event: ${event.action}")
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if (event.x > nav_view?.width!!) {
                            operateDrawer(false);
                        }
                    }

                    else -> {

                    }
                }
                false
            }
        }
        main_view?.let {

        }
        var lastX: Float = 0f
        drawerLayout.let {
            it?.setOnTouchListener { v, event ->
                val currentX = event.x;
                if (currentX < lastX) {
//                    关闭抽屉
                    main_view?.translationX = currentX;
//                    operateDrawer(false);
                }
                nav_view?.translationX = -currentX;
                Timber.d("drawerLayout touch event: ${event.action} x=${event.x} y=${event.y}");
                lastX = currentX

                false
            }
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
                handleViewpagerTouchEvent(event)
                false // 返回false，不拦截事件，保证ViewPager正常滑动
            }
            it?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int, positionOffset: Float, positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    navigationAdapter?.setSelectedPosition(position)
                    /*if (drawerOpening) {
                        switchFragment(0)
                    }*/
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
        navigationAdapter?.setSelectedPosition(fragmentIndex)

        /*when (fragmentIndex) {
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
        animationJob?.cancel() // 销毁时
        viewPager?.adapter = null
        navigationView?.adapter = null
        navigationAdapter?.setOnNavigationListener(null)
    }

    // 确保视图测量完成后获取抽屉宽度
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            drawerMaxTranslationX = -drawerWidth // 抽屉初始位置：-width（完全隐藏）
        }
    }

    // 滑动相关变量
    private var startX = 0f // 触摸起始X坐标
    private var currentX = 0f
    private var isDragging = false // 是否正在拖动
    private var velocityTracker: VelocityTracker? = null // 速度追踪器
    private var isDrawerMoving = false;
    private val touchSlop by lazy { ViewConfiguration.get(this).scaledTouchSlop }
    private val minFlingVelocity by lazy { ViewConfiguration.get(this).scaledMinimumFlingVelocity }
    private val drawerWidth by lazy { nav_view?.width ?: 0 } // 抽屉宽度（dp转px）

    // 动画相关
    private val interpolator: Interpolator = DecelerateInterpolator(1.5f)
    private var animationJob: Job? = null

    /**
     *  抽屉最大可移动距离（负数，抽屉宽度的相反数）
     */
    private var drawerMaxTranslationX = 0

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
    private fun handleViewpagerTouchEvent(event: MotionEvent): Boolean {
        // 确保抽屉宽度已获取
        if ((viewPager?.currentItem ?: -1) > 0 || drawerWidth == 0) return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 取消可能正在进行的动画
                animationJob?.cancel()

                // 记录初始位置
                startX = event.x
                currentX = startX
                isDragging = false
                velocityTracker = VelocityTracker.obtain()
                velocityTracker?.addMovement(event)
            }

            MotionEvent.ACTION_MOVE -> {
                velocityTracker?.addMovement(event)
                val x = event.x
                val dx = x - startX // 滑动距离（正值向右）

                // 判断是否开始拖动（超过系统最小滑动距离）
                if (!isDragging && abs(dx) > touchSlop) {
                    isDragging = true
                    // 仅在向右滑时处理（假设第一个页面）
                    val vpCurrent = viewPager?.currentItem ?: -1;
                    if (vpCurrent == 0 || vpCurrent == 4) {//|| dx > 0
                        isDrawerMoving = true
                    }
                }
                /**
                 *  slideOffset 抽屉滑动比例  0-1 0是复位
                 */
                val slideOffset = min(1f, max(0f, dx / drawerWidth))
                /**
                 *  随着手指滑动宽度 w1  抽屉滑动距离 d1 = w1 * drawerWidth
                 */

                Timber.d("drawerWidth=$drawerWidth dx=$dx slideOffset=$slideOffset ")
                nav_view?.translationX = drawerMaxTranslationX + (drawerWidth * slideOffset)
                // 处理抽屉滑动
                if (isDrawerMoving) {
                    currentX = x
                    // 计算滑动比例（0-1）：0=完全关闭，1=完全打开
//                    val slideOffset = min(1f, max(0f, dx / drawerWidth))

                    // 手动更新抽屉位置：从 -width（隐藏）到 0（完全显示）
//                    nav_view?.translationX = drawerMaxTranslationX + (drawerWidth * slideOffset)

                    // 应用立体感效果
                    applyStereoEffect(slideOffset)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isDrawerMoving) {
                    velocityTracker?.apply {
                        computeCurrentVelocity(1000)
                        val xVelocity = xVelocity // 滑动速度（正值向右）

                        // 计算当前滑动比例
                        val dx = currentX - startX
                        val slideOffset = min(1f, max(0f, dx / drawerWidth))

                        // 判断最终状态：速度达标或滑动超过一半则打开，否则关闭
                        val shouldOpen = xVelocity > minFlingVelocity || slideOffset > 0.5f
                        // 执行打开/关闭动画
//               暂时不用动画         animateDrawer(shouldOpen, slideOffset)
                        if (shouldOpen) {
                            operateDrawer(true)
                        } else {
                            operateDrawer(false)
                        }
                        recycle()
                    }
                }

                // 重置状态
                isDragging = false
                isDrawerMoving = false
                velocityTracker = null
            }
        }
        return true
    }


    /**
     * 抽屉打开/关闭动画
     * @param open 是否打开
     * @param startOffset 动画起始比例
     */
    private fun animateDrawer(open: Boolean, startOffset: Float) {
        val duration = 300L // 动画时长
        val startTime = System.currentTimeMillis()
        val endOffset = if (open) 1f else 0f

        // 使用协程实现动画
        animationJob = CoroutineScope(Dispatchers.Main).launch {
            while (System.currentTimeMillis() - startTime < duration) {
                val elapsed = System.currentTimeMillis() - startTime
                val fraction = interpolator.getInterpolation(elapsed.toFloat() / duration)
                val currentOffset = startOffset + (endOffset - startOffset) * fraction

                // 更新抽屉位置
                nav_view?.translationX = drawerMaxTranslationX + (drawerWidth * currentOffset)
                // 更新立体效果
                applyStereoEffect(currentOffset)

                delay(16) // 约60fps
            }

            // 动画结束后确保最终状态正确
            nav_view?.translationX = if (open) 0f else drawerMaxTranslationX.toFloat()
            applyStereoEffect(endOffset)
            if (open) {
                operateDrawer(true)
            } else {
                operateDrawer(false)
            }
        }
    }

    private var drawerOpening = false;
    private fun operateDrawer(open: Boolean) {
        if (drawerLayout?.isDrawerOpen(nav_view!!) == open) return
        if (open) {
            drawerOpening = true;
            drawerLayout?.openDrawer(nav_view!!)
        } else {
            drawerLayout?.closeDrawer(nav_view!!)
            drawerOpening = false;
        }
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

        // 3. 主界面向右位移（增强被推开的感觉）
        main_view?.translationX = drawerLayout?.width!! * slideOffset * 0.3f

        // 4. 抽屉阴影增强（随打开比例增加阴影深度）
        drawerLayout?.elevation = 5f + slideOffset * 15f
    }


}