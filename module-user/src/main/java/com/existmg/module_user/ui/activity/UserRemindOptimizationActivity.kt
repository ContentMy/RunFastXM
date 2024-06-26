package com.existmg.module_user.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.existmg.library_common.activity.BaseMVVMActivity
import com.existmg.library_common.managers.viewModelFactory
import com.existmg.library_common.router.RouterFragmentPath
import com.existmg.library_common.utils.PermissionUtils
import com.existmg.library_common.utils.ToastUtil
import com.existmg.module_user.R
import com.existmg.module_user.databinding.UserLayoutRemindOptimizationActivityBinding
import com.existmg.module_user.viewmodel.UserRemindOptimizationViewModel

/**
 * @Author ContentMy
 * @Date 2024/6/3 5:22 PM
 * @Description
 */
@Route(path = RouterFragmentPath.User.OPTIMIZATION_USER)
class UserRemindOptimizationActivity:
    BaseMVVMActivity<UserRemindOptimizationViewModel, UserLayoutRemindOptimizationActivityBinding>(),
    View.OnClickListener {

    override fun getViewModelClass(): Class<UserRemindOptimizationViewModel> {
        return UserRemindOptimizationViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            UserRemindOptimizationViewModel()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.user_layout_remind_optimization_activity
    }

    override fun initView() {
        checkAndRequestPermissions()
    }
    override fun initData() {
        mBinding.userOptimizationToolbar.uiToolbarTvTitle.text = resources.getText(R.string.user_setting_string_optimization)
        mBinding.userOptimizationTvNotificationSwitch.text = if (NotificationManagerCompat.from(this).areNotificationsEnabled()){
            resources.getText(R.string.user_optimization_notification_switch_open_string)
        }else{
            resources.getText(R.string.user_optimization_notification_switch_close_string)
        }
    }

    override fun initListener() {
        mBinding.userOptimizationClNotification.setOnClickListener(this)
        mBinding.userOptimizationClSelfStart.setOnClickListener(this)
        mBinding.userOptimizationToolbar.uiToolbarIvBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            mBinding.userOptimizationClNotification->{
                val intent = Intent()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                } else {
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                }
                startActivity(intent)
            }

            mBinding.userOptimizationClSelfStart->{
                //TODO：自启动跳转目前是跳转到应用设置页面，用户手动开启，并且获取不到是否开启的状态。
                // 目前小米是最好操作的，华为测试也可以跳转，但自启动不在应用设置页面，后续进行兼容优化
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }

            mBinding.userOptimizationToolbar.uiToolbarIvBack->{
                finish()
            }
        }
    }

    /*============================针对Android13以及以上版本的通知权限处理逻辑================================*/
    private fun checkAndRequestPermissions() {
        if(NotificationManagerCompat.from(this).areNotificationsEnabled()){//如果通知开启的情况下，那么不需要在进行动态请求
            return
        }
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (!PermissionUtils.hasPermissions(this, permissions.toTypedArray())) {
            PermissionUtils.requestPermissions(this, permissions.toTypedArray(), requestPermissionsLauncher)
        } else {
            // 所有权限已被授予
            // 执行与权限相关的操作
            if (NotificationManagerCompat.from(this).areNotificationsEnabled()){
                ToastUtil.showShort(this,"已经拥有通知权限，可以创建你的提醒了哦！")
            }else{
                ToastUtil.showShort(this,"没有获取到通知权限，会影响到你的使用体验哦！")
            }
        }
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            val isGranted = it.value
            if (isGranted) {
                // 权限被授予
                // 执行与权限相关的操作
                if (NotificationManagerCompat.from(this).areNotificationsEnabled()){
                    ToastUtil.showShort(this,"已经拥有通知权限，可以创建你的提醒了哦！")
                }else{
                    ToastUtil.showShort(this,"没有获取到通知权限，会影响到你的使用体验哦！")
                }

            } else {
                // 权限被拒绝
                // 处理权限被拒绝的情况
                ToastUtil.showShort(this,"没有获取到通知权限，会影响到你的使用体验哦！")
            }
        }
    }
}