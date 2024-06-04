package com.existmg.module_remind.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.existmg.library_common.activity.BaseMVVMActivity
import com.existmg.library_common.managers.ActivityStackManager
import com.existmg.library_common.managers.viewModelFactoryWithParams
import com.existmg.library_common.router.RouterFragmentPath
import com.existmg.library_common.utils.ToastUtil
import com.existmg.library_data.accessor.RemindModuleRoomAccessor
import com.existmg.library_data.db.entity.RemindEntity
import com.existmg.library_data.repository.RemindRepository
import com.existmg.library_ui.views.countdown.CountDownClock
import com.existmg.module_remind.R
import com.existmg.module_remind.databinding.RemindActivityDetailBinding
import com.existmg.module_remind.utils.logs.RemindLoggerManager
import com.existmg.module_remind.viewmodel.RemindDetailViewModel

/**
 * @Author:ContentMy
 * @Date: 2024/4/16 11:44 AM
 * @Description:这是倒计时提醒的详情界面，会显示一个实时的距离提醒剩余时间的倒计时页面，具体如下
 * 左上角：退出按钮，退回到提醒列表页面
 * 标题：显示提醒的名称
 * 右上角：修改按钮，跳转到RemindCreateActivity，可以提醒进行修改
 *
 */
class RemindDetailActivity : BaseMVVMActivity<RemindDetailViewModel, RemindActivityDetailBinding>(),
    View.OnClickListener {
    private val mLog = RemindLoggerManager.getLogger<RemindDetailActivity>()
    private var isRunning: Boolean = true
    private var mCountDownTime = 0L

    override fun getLayoutId(): Int {
        return R.layout.remind_activity_detail
    }

    override fun getViewModelClass(): Class<RemindDetailViewModel> {
        return RemindDetailViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactoryWithParams(RemindModuleRoomAccessor.getRemindRepository()){
            RemindDetailViewModel(it[0] as RemindRepository)
        }
    }

    override fun initView(){
        val typeface = ResourcesCompat.getFont(this, R.font.ui_roboto_bold)
        mBinding.remindDetailCdc.setCustomTypeface(typeface!!)
    }

    override fun initData(){
        val remindData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("remindEntity",RemindEntity::class.java)
        } else {
            intent.getParcelableExtra<RemindEntity>("remindEntity")
        }
        if (remindData != null){//如果不为空，是从提醒列表item点击进入的页面
           mViewModel.initData(remindData)
            mBinding.remindDetailTb.uiToolbarTvTitle.text = remindData.remindTitle
        }else{//如果为空，是从通知点击进入的页面
            val dataId = intent.getIntExtra("dataId",0)
            mLog.debug("通知进入 id为：$dataId")
            mViewModel.queryRemindEntity(dataId)
        }
//        mBinding.remindDetailTb.uiToolbarTvRight.text = resources.getString(R.string.remind_string_exchanged)
    }

    override fun initListener(){
        mBinding.remindDetailCdc.setCountdownListener(object: CountDownClock.CountdownCallBack{
            override fun countdownAboutToFinish() {
            }

            override fun countdownFinished() {
                ToastUtil.showShort(this@RemindDetailActivity, "Finished")
                mBinding.remindDetailCdc.resetCountdownTimer()
                isRunning = false
                mBinding.remindDetailCdc.visibility = View.GONE
                mBinding.remindDetailCompletedContent.visibility = View.VISIBLE
            }
        })

        mBinding.remindDetailTb.uiToolbarIvBack.setOnClickListener(this)
        mBinding.remindDetailTb.uiToolbarTvRight.setOnClickListener(this)
    }

    override fun initObserver() {
        mViewModel.remindById.observe(this){
            if (it != null){
                mBinding.remindDetailTb.uiToolbarTvTitle.text = it.remindTitle
            }else{
                //这里处理的逻辑原因场景为：通知栏已经弹出了通知，但是用户删除了对应的提醒
                //此时从通知点击进入时，id是携带者删除之前的那个id，但是用这个id去查询时，就会返回空
                //这个页面也就没有展示必要了，所以结束这个页面 TODO：考虑跳转到Main模块的MainActivity同时结束这个activity
                finishActivity()
            }
        }

        mViewModel.remindShowTime.observe(this){
            if (it > 0){
                mCountDownTime = it
                mBinding.remindDetailCdc.startCountDown(mCountDownTime)
            }else{
                mBinding.remindDetailCdc.visibility = View.GONE
                mBinding.remindDetailCompletedContent.visibility = View.VISIBLE
            }
        }
    }
    override fun onClick(v: View?) {
        when(v){
            mBinding.remindDetailTb.uiToolbarIvBack->{
                finishActivity()
            }
            mBinding.remindDetailTb.uiToolbarTvRight->{
                startActivityForName(this,RemindCreateActivity::class.java)
            }


        }
    }

    private fun finishActivity(){
        finish()
    }

    override fun finish() {
        val isFromNotification = intent.getBooleanExtra("fromNotification",false)
        if (isFromNotification && ActivityStackManager.activityStackSize() <= 1){
            // 如果是从通知启动的，并且此时堆栈中只有一个activity，那么在返回时启动MainActivity
            ARouter.getInstance().build(RouterFragmentPath.Main.PAGE_MAIN).navigation()
        }
        super.finish()
    }

    private fun startActivityForName(
        context: Context,
        javaClass: Class<*>
    ) {
        val intent = Intent(context,javaClass)
        context.startActivity(intent)
    }
}