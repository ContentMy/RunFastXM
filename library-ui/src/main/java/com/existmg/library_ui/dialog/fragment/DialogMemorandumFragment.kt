package com.existmg.library_ui.dialog.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.inputmethod.InputMethodManager
import com.existmg.library_ui.R
import com.existmg.library_ui.databinding.UiDialogMemorandumFragmentBinding
import com.existmg.library_ui.dialog.base.DialogBaseMvvmFragment
import com.existmg.library_ui.dialog.enums.DialogGravityId
import com.existmg.library_ui.dialog.interfaces.DialogDatabaseDelegate
import com.existmg.library_ui.dialog.viewmodel.DialogMemorandumViewModel


/**
 * @Author ContentMy
 * @Date 2024/4/25 10:22 AM
 * @Description 包内可见的DialogMemorandumFragment，继承于Mvvm的模式封装的基类
 * TODO:暂时先放在UI模块下，后续可能考虑移动到功能模块。因为目前感觉可能更偏向于业务定制化功能的dialog，缺少了通用性
 */
internal class DialogMemorandumFragment :DialogBaseMvvmFragment<DialogMemorandumViewModel,UiDialogMemorandumFragmentBinding>(){
    private lateinit var mDialogDatabaseDelegate: DialogDatabaseDelegate

    override fun getLayoutId(): Int {
        return R.layout.ui_dialog_memorandum_fragment
    }

    override fun getViewModelClass(): Class<DialogMemorandumViewModel> {
        return DialogMemorandumViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
        // 观察关于输入框焦点的数据变化
        mViewModel.editTextFocus.observe(viewLifecycleOwner) { hasFocus ->
            if (hasFocus) {
                mBinding.uiDialogMemorandumEtTitle.requestFocus()
            }
        }

        //观察数据变化，如果生成了入库数据，就调用委托接口，交给提醒模块进行处理
        mViewModel.processData.observe(this){
            if (mDialogDatabaseDelegate != null){
                mDialogDatabaseDelegate.processBusinessLogic(it)
                dismiss()
            }
        }

        mViewModel.editTextContent.observe(this){
            val title = it.toString().trim()
            if (title.isEmpty()){
                mViewModel.hasSendData.set(false)
            }else{
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
                    uiDialogMemorandumEtTitle.requestFocus()//不起作用，需要handle延时提醒，因为dialog和软键盘会冲突
                    it.showSoftInput(uiDialogMemorandumEtTitle, InputMethodManager.SHOW_IMPLICIT)
                },200)
            }
        }
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
//
//        // 设置Dialog取消或消失时隐藏软键盘
//        dialog.setOnCancelListener {
//            // 假设当前DialogFragment显示的EditText是focusView
//            val focusView = requireActivity().currentFocus
//            if (inputMethodManager != null && focusView != null) {
//                inputMethodManager?.hideSoftInputFromWindow(focusView.windowToken, 0)
//            }
//        }
//
//        dialog.setOnDismissListener {
//            // 假设当前DialogFragment显示的EditText是focusView
//            val focusView = requireActivity().currentFocus
//            if (inputMethodManager != null && focusView != null) {
//                inputMethodManager?.hideSoftInputFromWindow(focusView.windowToken, 0)
//            }
//        }
        return dialog
    }
    override fun onPause() {
        super.onPause()
    }

    override fun hasDialogStyle(): Boolean {
        return true
    }

    override fun getDialogStyleId(): Int {
        return R.style.ui_BottomDialogTheme
    }

    fun setDialogDatabaseDelegate(delegate: DialogDatabaseDelegate){
        mDialogDatabaseDelegate = delegate
    }
}