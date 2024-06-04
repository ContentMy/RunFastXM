package com.existmg.library_ui.views

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.Scroller
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * @Author ContentMy
 * @Date 2024/5/6 12:02 AM
 * @Description
 */
class SlideRecycleView : RecyclerView{
    private val INVALID_POSITION = -1 //用于判断触摸点位置是否在item范围内
    private val INVALID_CHILD_WIDTH = -1 //左滑隐藏控件的默认宽度
    private val SNAP_VELOCITY = 1000 //最小滑动速度

    private var mVelocityTracker:VelocityTracker? = null //速度追踪器
    private var mTouchSlop = 0 //认为是最小的滑动距离（一般由系统提供）
    private var mTouchFrame: Rect? = null //item所在的矩形范围
    private var mScroller:Scroller? = null //用于处理item的滑动效果
    private var mLastX = 0f //滑动过程中上次碰触点的x
    private var mFirstX = 0f //首次触碰范围
    private var mFirstY = 0f
    private var mIsSlide = false //是否滑动item
    private var mFlingView:ViewGroup? = null //触碰的item
    private var mPosition = 0 // 触碰的item的位置
    private var mMenuViewWidth = 0 // 隐藏控件的宽度
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mScroller = Scroller(context)
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        if (e == null){
            return false
        }
        val x = e.x
        val y = e.y
        obtainVelocity(e)
        when(e.action){
            MotionEvent.ACTION_DOWN->{
                mLastX = x
                mFirstX = x
                mFirstY = y
                mPosition = pointToPosition(x.toInt(),y.toInt())//触碰点所在的position
                if(mPosition != INVALID_POSITION){
                    val view = mFlingView
                    //因为这里的布局是使用了CardView嵌套了了一个LinearLayout，所以需要先拿到外层的viewGroup，再通过这个对象去获取LinearLayout
//                    val childParentLayout = getChildAt(mPosition - (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()) as ViewGroup
                    mFlingView = getChildAt(mPosition - (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()) as ViewGroup?
                    // 这里判断一下如果之前触碰的view已经打开，而当前碰到的view不是那个view则立即关闭之前的view，
                    // 此处并不需要担心动画没完成冲突，因为之前已经abortAnimation
                    if (view != null && mFlingView != view && view.scaleX != 0F){
                        view.scrollTo(0,0)
                    }
                    // 这里进行了强制的要求，RecyclerView的子ViewGroup必须要有2个子view,这样菜单按钮才会有值，
                    // 需要注意的是:如果不定制RecyclerView的子View，则要求子View必须要有固定的width。
                    // 比如使用LinearLayout作为根布局，而content部分width已经是match_parent，此时如果菜单view用的是wrap_content，menu的宽度就会为0。
                    mMenuViewWidth = if (mFlingView?.childCount == 2){
                        mFlingView?.getChildAt(1)?.width!!
                    }else{
                        INVALID_CHILD_WIDTH
                    }
//                    println("子view数量 $mFlingView?.childCount")
                }
            }
            MotionEvent.ACTION_MOVE->{
//                println("拦截move")
                mVelocityTracker?.computeCurrentVelocity(1000)//计算当前滑动的速度，秒为单位
                // 此处有俩判断，满足其一则认为是侧滑：
                // 1.如果x方向速度大于y方向速度，且大于最小速度限制；
                // 2.如果x方向的侧滑距离大于y方向滑动距离，且x方向达到最小滑动距离；
                val xVelocity = mVelocityTracker?.xVelocity
                val yVelocity = mVelocityTracker?.yVelocity
                if (abs(xVelocity!!) > SNAP_VELOCITY && abs(xVelocity) > abs(yVelocity!!)
                    || abs(x-mFirstX) >= mTouchSlop
                    && abs(x-mFirstX) > abs(y-mFirstY)){
                    mIsSlide = true;
                    return true
                }
            }
            MotionEvent.ACTION_UP->{
//                println("拦截up")
                releaseVelocity()
                return false
            }
        }
        return super.onInterceptTouchEvent(e)
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        if (e == null){
            return false
        }
        if (mIsSlide && mPosition != INVALID_POSITION){
            val x = e.x
            obtainVelocity(e)
            when(e.action){
                MotionEvent.ACTION_DOWN->{//因为没有拦截，所以不会被调用到
//                    println("touch down")
                    mLastX = x
                }
                MotionEvent.ACTION_MOVE->{
//                    println("touch move")
                    //随手指滑动
                    if (mMenuViewWidth != INVALID_CHILD_WIDTH ){
                        val dx = mLastX - x
                        val scrollX = mFlingView?.scrollX ?: 0
                        // 计算滑动后的位置
                        val newScrollX = scrollX + (dx).toInt()
                        // 边界检查，确保滑动不超出范围
                        val validScrollX = newScrollX.coerceIn(0, mMenuViewWidth)
                        mFlingView?.scrollTo(validScrollX, 0)
                        mLastX = x
                    }
                    // 更新 mLastX 的值
//                    mLastX = x
                }
                MotionEvent.ACTION_UP->{
//                    println("touch up")
                    if (mMenuViewWidth != INVALID_CHILD_WIDTH){
                        val scrollX = mFlingView?.scrollX
                        mVelocityTracker?.computeCurrentVelocity(1000)
                        // 此处有两个原因决定是否打开菜单：
                        // 1.菜单被拉出宽度大于菜单宽度一半；
                        // 2.横向滑动速度大于最小滑动速度；
                        // 注意：之所以要小于负值，是因为向左滑则速度为负值
                       if (mVelocityTracker?.xVelocity!! < -SNAP_VELOCITY){// 向左侧滑达到侧滑最低速度，则打开
                           mScroller?.startScroll(scrollX!!,0,mMenuViewWidth-scrollX,0, abs(mMenuViewWidth - scrollX))
                       }else if (mVelocityTracker?.xVelocity!! >= SNAP_VELOCITY){// 向右侧滑达到侧滑最低速度，则关闭
                           mScroller?.startScroll(scrollX!!,0,-scrollX,0, abs(scrollX))
                       }else if (scrollX!! >= mMenuViewWidth/2){// 如果超过删除按钮一半，则打开
                           mScroller?.startScroll(scrollX,0,mMenuViewWidth-scrollX,0, abs(mMenuViewWidth - scrollX))
                       }else{//其他情况，关闭
                           mScroller?.startScroll(scrollX,0,-scrollX,0, abs(scrollX))
                       }
                       invalidate()
                    }
                    mMenuViewWidth = INVALID_CHILD_WIDTH
                    mIsSlide = false
                    mPosition = INVALID_POSITION
                    releaseVelocity()
                }
            }
            return true
        }else{
            // 此处防止RecyclerView正常滑动时，还有菜单未关闭
            closeMenu()
            // Velocity，这里的释放是防止RecyclerView正常拦截了，但是在onTouchEvent中却没有被释放；
            // 有三种情况：1.onInterceptTouchEvent并未拦截，在onInterceptTouchEvent方法中，DOWN和UP一对获取和释放；
            // 2.onInterceptTouchEvent拦截，DOWN获取，但事件不是被侧滑处理，需要在这里进行释放；
            // 3.onInterceptTouchEvent拦截，DOWN获取，事件被侧滑处理，则在onTouchEvent的UP中释放。
            releaseVelocity()
        }

        return super.onTouchEvent(e)
    }

    private fun closeMenu() {
        if (mFlingView != null && mFlingView?.scrollX != 0){
            mFlingView?.scrollTo(0,0)
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller != null && mScroller!!.computeScrollOffset()) {
            mFlingView?.scrollTo(mScroller!!.currX, mScroller!!.currY)
            postInvalidate()
        }
    }

    /**
     * @Author: ContentMy
     * @params: event 手势事件
     * @Description: 初始化Velocity并处理event，方便后续计算手势速度
     */
    private fun obtainVelocity(event: MotionEvent) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()//创建VelocityTracker对象
        }
        mVelocityTracker!!.addMovement(event)//将event加入到VelocityTracker中，方便后续计算数据
    }

    private fun releaseVelocity() {
        mVelocityTracker?.recycle()
        mVelocityTracker?.clear()
        mVelocityTracker = null
    }

    private fun pointToPosition(x: Int, y: Int): Int {
        val firstPosition: Int = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        var frame = mTouchFrame
        if (frame == null) {
            mTouchFrame = Rect()
            frame = mTouchFrame
        }
        val count = childCount
        for (i in count - 1 downTo 0) {
            val child: View = getChildAt(i)
            if (child.visibility == View.VISIBLE) {
                child.getHitRect(frame)
                if (frame!!.contains(x, y)) {
                    return firstPosition + i
                }
            }
        }
        return INVALID_POSITION
    }
}