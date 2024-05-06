package com.existmg.module_remind.ui

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.existmg.library_base.activity.BaseMVVMActivity
import com.existmg.library_base.manager.viewModelFactoryWithParams
import com.existmg.library_data.accessor.RemindModuleRoomAccessor
import com.existmg.library_data.repository.RemindRepository
import com.existmg.library_ui.databinding.UiLayoutHorizontalTimeSelectItemBinding
import com.existmg.library_ui.dialog.adapter.HorizontalItemTimeSelectRecycleViewAdapter
import com.existmg.library_ui.view.CustomLinearLayout
import com.existmg.module_remind.R
import com.existmg.module_remind.databinding.RemindActivityRemindCreateBinding
import com.existmg.module_remind.viewmodel.RemindCreateViewModel

/**
 * @Author:ContentMy
 * @Date: 2024/4/27 12:34 PM
 * @Description: 这里是dialog样式的新建提醒的activity页面
 */
class RemindCreateActivity : BaseMVVMActivity<RemindCreateViewModel,RemindActivityRemindCreateBinding>(),
    HorizontalItemTimeSelectRecycleViewAdapter.OnHorizontalItemSelectedCallback,
    View.OnClickListener {
    private lateinit var mAdapter: HorizontalItemTimeSelectRecycleViewAdapter//TODO：优化时要考虑做封装，与UI模块解耦。方案：考虑放入common模块，资源统一在使用时去ui模块获取或者放到功能模块下
    private var currentPosition = 0
    private val inputMethodManager:InputMethodManager? by lazy{
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }
    override fun getViewModelClass(): Class<RemindCreateViewModel> {
        return RemindCreateViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactoryWithParams(RemindModuleRoomAccessor.getRemindRepository(),application) {
            RemindCreateViewModel(it[0] as RemindRepository,it[1] as Application)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.remind_activity_remind_create
    }

    override fun initView() {
        //初始化适配器时接入默认的时间选择
        mAdapter = HorizontalItemTimeSelectRecycleViewAdapter(mutableListOf(
            "5分钟","10分钟","15分钟","20分钟","25分钟","30分钟","45分钟","1小时"
        ))
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        mBinding.remindCreateRv.layoutManager = layoutManager
        mBinding.remindCreateRv.adapter = mAdapter
    }

    override fun initData() {
    }

    override fun initListener() {
        mAdapter.setOnHorizontalItemSelectedCallback(this)
        mAdapter.setOnItemClickListener { adapter, view, position ->
            currentPosition = position
            adapter.notifyDataSetChanged()
        }
        mBinding.remindCreateLl.setOnOutsideClickListener(object : CustomLinearLayout.OnOutsideClickListener{
            override fun onOutsideClick(view: View) {
                finish()
            }
        })
        mBinding.remindCreateCl.setOnClickListener(this)
        mBinding.remindCreateIvSave.setOnClickListener(this)
    }

    override fun initObserver() {

        mViewModel.editTextContent.observe(this){
            val title = it.toString().trim()
            if (title.isEmpty()){
                mBinding.remindCreateIvSave.setImageResource(R.drawable.ui_send_gray)
                mViewModel.hasSendData.set(false)
            }else{
                mBinding.remindCreateIvSave.setImageResource(R.drawable.ui_send)
                mViewModel.hasSendData.set(true)
            }
        }


        // 观察关于输入框焦点的数据变化
        mViewModel.editTextFocus.observe(this) { hasFocus ->
            if (hasFocus) {
                mBinding.remindCreateEtTitle.requestFocus()
            }
        }
    }

    override fun horizontalItemSelected(
        holder: BaseDataBindingHolder<UiLayoutHorizontalTimeSelectItemBinding>,
        position: Int,
        displayName: String
    ) {
        if (position == currentPosition){
            holder.dataBinding?.uiItemHorizontalTimeSelectTvTime?.setTextColor(Color.WHITE)
            holder.dataBinding?.uiItemHorizontalTimeSelectCl?.setBackgroundResource(com.existmg.library_ui.R.drawable.ui_shape_rounded_rectangle_background_black)
            mViewModel.setTime(displayName)
        }else{
            holder.dataBinding?.uiItemHorizontalTimeSelectTvTime?.setTextColor(Color.BLACK)
            holder.dataBinding?.uiItemHorizontalTimeSelectCl?.setBackgroundResource(com.existmg.library_ui.R.drawable.ui_shape_rounded_rectangle_background_green)
        }
    }

    override fun onClick(v: View?) {
        when(v){
            mBinding.remindCreateIvSave->{
                if ( mViewModel.hasSendData.get()){//TODO:待优化，考虑将逻辑置入viewmodel中处理而不是在view层去做
                    mViewModel.insertRemind()
                    hideSoftInput(mBinding.remindCreateIvSave)
                    finish()
                }else{
                    Toast.makeText(this, "请输入想要提醒的事项哦", Toast.LENGTH_SHORT).show()
                }
            }

            mBinding.remindCreateCl->{
                hideSoftInput(mBinding.remindCreateCl)
                mBinding.remindCreateEtTitle.clearFocus()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        inputMethodManager?.let {
            mBinding.remindCreateEtTitle.requestFocus()
            it.showSoftInput(mBinding.remindCreateEtTitle, InputMethodManager.SHOW_IMPLICIT)
        }
    }
    override fun finish() {
        super.finish()
        hideSoftInput(mBinding.remindCreateEtTitle)
        overridePendingTransition(R.anim.ui_alpha_show,R.anim.ui_alpha_hide)//解决退出activity时黑屏的问题
    }

    private fun hideSoftInput(v: View){//TODO:后续增加判断，如果软键盘显示的情况下再调用
        inputMethodManager?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}