package com.existmg.module_target.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.existmg.library_base.activity.BaseMVVMActivity
import com.existmg.library_base.manager.viewModelFactoryWithParams
import com.existmg.library_common.utils.setLightStatusBar
import com.existmg.library_data.local.IconsManager
import com.existmg.library_ui.dialog.view.MyDataPickerDialog
import com.existmg.library_ui.interfaces.DataPickerGetCalendarListener
import com.existmg.module_target.R
import com.existmg.library_data.accessor.TargetModuleRoomAccessor
import com.existmg.module_target.databinding.TargetActivityTargetCreateBinding
import com.existmg.library_ui.notification.NotificationReceiver
import com.existmg.library_ui.notification.NotificationRepository
import com.existmg.library_ui.utils.setDrawableImageByName
import com.existmg.library_ui.utils.setDrawableImageCircleById
import com.existmg.module_target.databinding.TargetIconsRecycleItemViewBinding
import com.existmg.module_target.databinding.TargetStatusRecycleItemViewBinding
import com.existmg.library_data.enums.DayStatus
import com.existmg.library_data.db.entity.TargetEntity
import com.existmg.module_target.ui.adapter.TargetIconsRecycleViewAdapter
import com.existmg.module_target.ui.adapter.TargetStatusRecycleViewAdapter
import com.existmg.library_common.utils.calenderToDateString
import com.existmg.library_data.repository.TargetRepository
import com.existmg.module_target.viewmodel.TargetCreateViewModel
import java.util.Calendar

/**
 * @Author:ContentMy
 * @Date: 2024/4/7 1:40 上午
 * @Description: 这里是新建目标页面，修改目标时也是复用了这个页面
 */
class TargetCreateActivity : BaseMVVMActivity<TargetCreateViewModel,TargetActivityTargetCreateBinding>(),
    View.OnClickListener,
    TargetStatusRecycleViewAdapter.OnItemSelectedCallback,
    TargetIconsRecycleViewAdapter.OnIconsItemSelectedCallback {
    private val notificationReceiver = NotificationReceiver()
    private var isNewTarget = true
    private lateinit var statusAdapter: TargetStatusRecycleViewAdapter
    private lateinit var iconsAdapter: TargetIconsRecycleViewAdapter
    private var currentStatusPosition = 0
    private var currentIconsPosition = 0
    override fun getLayoutId(): Int {
        return R.layout.target_activity_target_create
    }

    override fun getViewModelClass(): Class<TargetCreateViewModel> {
        return TargetCreateViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactoryWithParams(TargetModuleRoomAccessor.getTargetRepository(),
            NotificationRepository(),application){
            TargetCreateViewModel(it[0] as TargetRepository,it[1] as NotificationRepository,it[2] as Application)
        }
    }

    override fun initView() {
        //初始化目标状态recycleView相应设置并绑定adapter
        statusAdapter = TargetStatusRecycleViewAdapter(DayStatus.values().toMutableList())
        val statusLayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)//设置水平方向布局
        mBinding.targetCreateRlStatus.layoutManager = statusLayoutManager
        mBinding.targetCreateRlStatus.adapter = statusAdapter
        mBinding.targetCreateRlStatus.scrollToPosition(currentStatusPosition)

        //初始化目标状态recycleView相应设置并绑定adapter
        iconsAdapter = TargetIconsRecycleViewAdapter(IconsManager().getIconList())
        val iconsLayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        mBinding.targetCreateRlIcons.layoutManager = iconsLayoutManager
        mBinding.targetCreateRlIcons.adapter = iconsAdapter
        mBinding.targetCreateRlIcons.scrollToPosition(currentIconsPosition)
        mBinding.targetCreateIvIcon.setDrawableImageCircleById(this,R.color.ui_gray_light)
    }

    /**
     * @Author: ContentMy
     * @Description: 对数据进行一些初始化操作
     */
    override fun initData() {
        isNewTarget = intent.getBooleanExtra("isNewTarget",true)
        mBinding.targetCreateToolbar.uiToolbarTvTitle.text = if (isNewTarget) "新建目标" else "编辑目标"
        mBinding.targetCreateToolbar.uiToolbarTvRight.text = "保存"
        if (isNewTarget){
            mViewModel.initNewTarget()
        }else{
            val target = intent.getParcelableExtra<TargetEntity>("targetEntity")
            if (target != null){
                mViewModel.initExistTarget(target)
                println("目标状态为：${target.targetStatus}")
                currentStatusPosition = target.targetStatus
                currentIconsPosition = IconsManager().getIconPosition(target.targetImg!!)//因为目前是固定的资源，所以直接去根据icon的name去获取了固定列表中的下标来平替上次选择的对应下标
            }
        }
    }

    /**
     * @Author: ContentMy
     * @Description: 注册监听操作，然后在回调里统一处理逻辑
     */
    override fun initListener() {
        //有个DataBinding的情况下还在这里使用手动调用监听的原因：通用的toolbar在ui模块，虽然viewmodel可以通过app:viewModel透传到通用布局中，
        // 但通用布局的声明拿不到viewModel的路径，因为公共模块是不能去引用功能模块的，所以这里是手动去做点击事件的处理
        mBinding.targetCreateToolbar.uiToolbarTvRight.setOnClickListener(this)
        mBinding.targetCreateBtSave.setOnClickListener(this)
        mBinding.targetCreateToolbar.uiToolbarIvBack.setOnClickListener(this)
        statusAdapter.setOnItemSelectedCallback(this)
        statusAdapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                currentStatusPosition = position
                adapter.notifyDataSetChanged()
            }

        })
        iconsAdapter.setOnIconsItemSelectedCallback(this)
        iconsAdapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                currentIconsPosition = position
                adapter.notifyDataSetChanged()
            }

        })

        val filter = IntentFilter().apply {
            addAction("")
        }
//        registerReceiver(notificationReceiver,filter)
    }

    /**
     * @Author: ContentMy
     * @Description: 注册观察并在对应的回调中完成对应的操作
     */
    override fun initObserver() {
        //处理数据入库并且满足开启提醒的条件下的事件回调
        mViewModel.target.observe(this){
            if (it.targetRemind){
                // TODO: 待完成对于通知的测试和优化 
                mViewModel.startNotification(it,application)
            }
            finish()
        }
        //处理Dialog的点击观察回调以及数据回传
        mViewModel.showDialog.observe(this){
            MyDataPickerDialog.Builder(this).setListener(object: DataPickerGetCalendarListener {
                override fun getCalendar(calendar: Calendar) {
                    println("回调到activity，日期为：${calenderToDateString(calendar)},来源为：${it.source}")
                    mViewModel.getCalendar(it.source,calendar)
                }
            }).build().show(it.timestamp)
        }

        //图片列表选择后，同时更新icon的ui显示
        mViewModel.targetImageString.observe(this){
            mBinding.targetCreateIvIcon.setDrawableImageByName(it)
        }
    }

    // TODO: 暂时将反注册直接放在这个生命周期回调，后续封装lifecycle再优化这里
    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver(notificationReceiver)
    }

    /**
     * @Author: ContentMy
     * @Description: 设置icon的圆环
     */
    private fun setIconOval(){
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.OVAL
        drawable.setStroke(5, ContextCompat.getColor(this, R.color.ui_gray_light))
        mBinding.targetCreateIvIcon.background = drawable
        mBinding.targetCreateIvIcon.setImageResource(R.drawable.ui_icon_sleep)
    }

    override fun onClick(v: View?) {//TODO:bug.在软键盘弹出时，点击其他非输入框，会有事件响应，保存关闭页面，软键盘也不会收回。待解决
        when(v){
            mBinding.targetCreateToolbar.uiToolbarTvRight ->
                mViewModel.saveTarget(isNewTarget)
            mBinding.targetCreateBtSave ->
                mViewModel.saveTarget(isNewTarget)
            mBinding.targetCreateToolbar.uiToolbarIvBack ->
                finish()

        }
    }

    override fun statusIconSelected(
        holder: BaseDataBindingHolder<TargetStatusRecycleItemViewBinding>,
        position: Int,
        displayName: String
    ) {
        if (position == currentStatusPosition){
            holder.dataBinding?.targetStatusRecycleTvName?.setTextColor(Color.WHITE)
            holder.dataBinding?.targetStatusRecycleCl?.setBackgroundResource(R.drawable.ui_shape_rounded_rectangle_background_black)
            println("名称：$displayName,位置：$position")
            mViewModel.chooseStatus(displayName)
        }else{
            holder.dataBinding?.targetStatusRecycleTvName?.setTextColor(Color.BLACK)
            holder.dataBinding?.targetStatusRecycleCl?.setBackgroundResource(R.drawable.ui_shape_rounded_rectangle_background_stroke)
        }
    }

    override fun iconItemSelected(
        holder: BaseDataBindingHolder<TargetIconsRecycleItemViewBinding>,
        position: Int,
        displayName: String
    ) {
        if (position == currentIconsPosition){
            holder.dataBinding?.targetIconsRecycleCl?.setBackgroundResource(R.drawable.ui_shape_circle_background_white_black)
            mViewModel.chooseIcon(displayName)
        }else{
            holder.dataBinding?.targetIconsRecycleCl?.setBackgroundResource(0)
        }
    }
}