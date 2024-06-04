package com.existmg.library_ui.dialog.fragment

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.existmg.library_ui.R
import com.existmg.library_ui.databinding.UiDialogBottmonFragmentBinding
import com.existmg.library_ui.databinding.UiLayoutHorizontalTimeSelectItemBinding
import com.existmg.library_ui.dialog.adapter.HorizontalItemTimeSelectRecycleViewAdapter
import com.existmg.library_ui.dialog.base.DialogBaseMvvmFragment
import com.existmg.library_ui.dialog.enums.DialogGravityId
import com.existmg.library_ui.dialog.interfaces.DialogDatabaseDelegate
import com.existmg.library_ui.dialog.viewmodel.DialogBottomViewModel


/**
 * @Author ContentMy
 * @Date 2024/4/25 10:22 AM
 * @Description 包内可见的DialogBottomFragment，继承于Mvvm的模式封装的基类
 * TODO:暂时先放在UI模块下，后续可能考虑移动到功能模块。因为目前感觉可能更偏向于业务定制化功能的dialog，缺少了通用性
 */
internal class DialogBottomFragment :DialogBaseMvvmFragment<DialogBottomViewModel,UiDialogBottmonFragmentBinding>(),
    HorizontalItemTimeSelectRecycleViewAdapter.OnHorizontalItemSelectedCallback {

    private var currentPosition = 0
    private lateinit var mDialogDatabaseDelegate: DialogDatabaseDelegate
    private lateinit var mAdapter:HorizontalItemTimeSelectRecycleViewAdapter

    override fun getLayoutId(): Int {
        return R.layout.ui_dialog_bottmon_fragment
    }

    override fun getViewModelClass(): Class<DialogBottomViewModel> {
        return DialogBottomViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun initView() {
        //初始化适配器时接入默认的时间选择
        mAdapter = HorizontalItemTimeSelectRecycleViewAdapter(mutableListOf(
            "5分钟","10分钟","15分钟","20分钟","25分钟","30分钟","45分钟","1小时"
        ))
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        mBinding.uiDialogBottomRv.layoutManager = layoutManager
        mBinding.uiDialogBottomRv.adapter = mAdapter
    }

    override fun initListener() {
        mAdapter.setOnHorizontalItemSelectedCallback(this)
        mAdapter.setOnItemClickListener { adapter, view, position ->
            currentPosition = position
            adapter.notifyDataSetChanged()
        }
    }

    override fun initObserver() {

        // 观察关于输入框焦点的数据变化
        mViewModel.editTextFocus.observe(viewLifecycleOwner) { hasFocus ->
            if (hasFocus) {
                mBinding.uiDialogBottomEtTitle.requestFocus()
            }
        }
        //观察数据变化，如果生成了入库数据，就调用委托接口，交给提醒模块进行处理
        mViewModel.processData.observe(this){
            hideSoftInput(mBinding.uiDialogBottomCl)//TODO:手动点击发送数据时软键盘消失
            mDialogDatabaseDelegate.processBusinessLogic(it)
            dismiss()
        }

        mViewModel.editTextContent.observe(this){
            val title = it.toString().trim()
            if (title.isEmpty()){
                mBinding.uiDialogBottomIvSave.setImageResource(R.drawable.ui_send_gray)
                mViewModel.hasSendData.set(false)
            }else{
                mBinding.uiDialogBottomIvSave.setImageResource(R.drawable.ui_send)
                mViewModel.hasSendData.set(true)
            }
        }
    }




    // TODO: 后续优化lifecycle生命周期统一管理
    override fun onStart() {
        super.onStart()
//        viewModel.setEditTextFocus(true)
    }

    private val inputMethodManager:InputMethodManager? by lazy{
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onResume() {
        super.onResume()
        inputMethodManager?.let {
            mBinding.apply {
                Handler().postDelayed({
                    uiDialogBottomEtTitle.requestFocus()//不起作用，需要handle延时提醒，因为dialog和软键盘会冲突
                    it.showSoftInput(uiDialogBottomEtTitle, InputMethodManager.SHOW_IMPLICIT)
                },200)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mBinding.uiDialogBottomEtTitle.clearFocus()//清除输入框焦点，辅助隐藏软键盘
    }

    override fun hasDialogStyle(): Boolean {
        return true
    }

    override fun getDialogStyleId(): Int {
        return R.style.ui_BottomDialogTheme
    }

    override fun hasDialogAnimation(): Boolean {
        return true
    }

    override fun getDialogAnimationStyleId(): Int {
        return R.style.ui_BottomDialogAnimation
    }

    override fun hasDialogGravity(): Boolean {
        setGravityId(DialogGravityId.BOTTOM)
        return true
    }


    override fun horizontalItemSelected(
        holder: BaseDataBindingHolder<UiLayoutHorizontalTimeSelectItemBinding>,
        position: Int,
        displayName: String
    ) {
        if (position == currentPosition){
            holder.dataBinding?.uiItemHorizontalTimeSelectTvTime?.setTextColor(Color.WHITE)
            holder.dataBinding?.uiItemHorizontalTimeSelectCl?.setBackgroundResource(R.drawable.ui_shape_rounded_rectangle_background_black)
            mViewModel.setTime(displayName)
        }else{
            holder.dataBinding?.uiItemHorizontalTimeSelectTvTime?.setTextColor(Color.BLACK)
            holder.dataBinding?.uiItemHorizontalTimeSelectCl?.setBackgroundResource(R.drawable.ui_shape_rounded_rectangle_background_green)
        }
    }


    fun setDialogDatabaseDelegate(delegate: DialogDatabaseDelegate){
        mDialogDatabaseDelegate = delegate
    }
    private fun hideSoftInput(view: View){
        // TODO: 这里是手动去调用隐藏软键盘的方法
        if (inputMethodManager != null) {
            if (view.windowToken!=null){
                inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
            }

        }
    }

}