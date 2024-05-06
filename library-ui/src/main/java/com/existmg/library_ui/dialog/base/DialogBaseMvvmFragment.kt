package com.existmg.library_ui.dialog.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.existmg.library_ui.dialog.enums.DialogGravityId


/**
 * @Author ContentMy
 * @Date 2024/4/25 10:27 AM
 * @Description
 */
abstract class DialogBaseMvvmFragment<VM : ViewModel, VB : ViewDataBinding> : DialogFragment(),LifecycleObserver {

    protected lateinit var mBinding: VB
    protected lateinit var mViewModel: VM
    private lateinit var mDialogGravityId: DialogGravityId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置对话框样式
        if (hasDialogStyle()){
            setStyle(STYLE_NORMAL, getDialogStyleId())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this)[getViewModelClass()]
        mBinding.lifecycleOwner = this
        bindViewModel()
        initView()
        initData()
        initListener()
        initObserver()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if(hasDialogGravity()){
            // 设置对话框的位置为底部
            val layoutParams = dialog.window?.attributes
            layoutParams?.gravity = getGravityIdInner()
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }

    // TODO: 后续考虑方案优化，使用lifecycle统一管理生命周期，同时解决lifecycle注解过时的问题
    override fun onResume() {
        super.onResume()
        // 设置进入和退出动画
        if (hasDialogAnimation()){
            dialog?.window?.attributes?.windowAnimations = getDialogAnimationStyleId()
        }
    }

    /**
     * @Author: ContentMy
     * @return: boolean类型，是否设置dialog的Style
     * 返回true，则需要实现getDialogStyleId，返回对应的id即可设置
     * 返回false，则不需要进行任何操作
     * @Description:这是一个提供给子类调用返回判断是否要设置dialogStyle的方法
     * （open的含义是为了在抽象类中灵活去定义方法，如果子类想要设置style或者其他属性，再去实现对应的方法。
     * 而且这样设置需要给一个默认值）
     */
    open fun hasDialogStyle():Boolean{
        return false
    }

    /**
     * @Author: ContentMy
     * @return: int类型，得到子类设置的dialogStyle资源id
     * @Description:这是一个提供给子类调用并且返回styleId的方法
     */
    open fun getDialogStyleId():Int{
        return 0
    }

    /**
     * @Author: ContentMy
     * @return: boolean类型，是否设置dialog动画
     * 返回true，则需要实现getDialogAnimationId方法，返回对应的id即可设置
     * 返回false，则不需要进行任何操作
     * @Description:这是一个提供给子类调用返回判断是否要设置dialog动画的方法
     */
    open fun hasDialogAnimation():Boolean{
        return false
    }

    /**
     * @Author: ContentMy
     * @return: int类型，得到子类设置的动画资源的styleId
     * @Description:这是一个提供给子类调用并且返回动画资源Id的方法
     */
    open fun getDialogAnimationStyleId():Int{
        return 0
    }

    /**
     * @Author: ContentMy
     * @return: boolean类型，是否设置dialog位置
     * 返回true，则需要实现setGravityId方法，并在本方法内调用传入对应的DialogGravityId
     * 返回false，则不需要进行任何操作
     * @Description:这是一个提供给子类调用返回判断是否要设置dialogGravity的方法
     */
    open fun hasDialogGravity(): Boolean{
        return false
    }

    /**
     * @Author: ContentMy
     * @param: DialogGravityId枚举类，提供给子类使用的位置范围。最好在hasDialogGravity方法内调用，一并传回枚举参数
     * @Description:这是一个提供给子类调用传递枚举参数的方法
     */
    open fun setGravityId(id: DialogGravityId){
        mDialogGravityId = id
    }
    /**
     * @Author: ContentMy
     * @return: int类型，得到子类设置的动画资源的styleId
     * @Description:这是一个内部的方法，根据子类调用传递枚举参数并且mapping得到对应的Gravity位置并返回
     */
    private fun getGravityIdInner(): Int{
        return when(mDialogGravityId){
            DialogGravityId.TOP->Gravity.TOP
            DialogGravityId.CENTER->Gravity.CENTER
            DialogGravityId.BOTTOM->Gravity.BOTTOM
            DialogGravityId.NONE -> Gravity.NO_GRAVITY
        }
    }

    /**
     * @Author: ContentMy
     * @return: int类型，得到子类设置的布局id
     * @Description:这是一个提供给子类调用并且返回布局Id的方法
     */
    abstract fun getLayoutId(): Int

    /**
     * @Author: ContentMy
     * @return: Class<VM>，得到子类对应的ViewModel的class
     * @Description:这是一个提供给子类调用并且返回对应的ViewModel的class的方法
     */
    abstract fun getViewModelClass(): Class<VM>

    /**
     * @Author: ContentMy
     * @Description:这是一个提供给子类调用绑定viewmodel的方法。绑定操作不在基类操作，交给子类去做
     */
    abstract fun bindViewModel()

    open fun initView(){

    }

    open fun initData(){

    }

    open fun initListener(){

    }

    open fun initObserver(){

    }

    // TODO: 后续考虑的优化方案，在ViewModelFactory的基础上增加对于多参的支持，以及如果基类不是去实现扩展函数，替代方案的解决
//    private fun setupViewModelFactory() {
//        viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
//    }
//
//
//    private fun createViewModel(): VM {
//        return ViewModelProvider(this, viewModelFactory)[getViewModelClass()]
//    }
}