package com.existmg.library_common.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @Author ContentMy
 * @Date 2024/3/24 11:55 下午
 * @Description 这里是基类Fragment，用于做通用的封装操作
 */
abstract class BaseFragment : Fragment(){

    private val TAG: String = "TestAAA"

    /**
     *  当Activity与Fragment关联时调用
     *  一般是用于初始化Loader
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e(TAG, "onAttach: ")
    }

    /**
     *  当Fragment创建时调用
     *  一般用于初始化Fragment
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: ")
    }

    /**
     *  当Fragment布局创建时调用
     *  一般用于加载Fragment布局
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG, "onCreateView: ")
        return inflater.inflate(getLayoutId(),null,false)
    }

    /**
     *  当Fragment布局创建后调用
     *  一般用于初始化ViewModel
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated: ")
        beforeInit()
        initView()
        initData()
        initListener()
    }

    /**
     *  TODO:提示过时，建议是使用onViewCreated来代替使用
     *  当Activity的onCreate方法执行完毕后调用
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(TAG, "onActivityCreated: ")
    }

    /**
     *  当Fragment可见时调用
     *  一般用于开始动画或者其他UI变化的操作
     */
    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart: ")
    }

    /**
     *  当Fragment可见并且和用户开始交互时调用
     *  一般用于onPause暂停的状态恢复操作
     */
    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: ")
    }

    /**
     *  当Fragment不可交互但依然可见时调用
     *  一般用于临时状态的保存操作
     */
    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause: ")
    }

    /**
     *  当Fragment不可见时调用
     *  一般用于释放资源的操作
     */
    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop: ")
    }

    /**
     *  当Fragment视图被移除时调用
     *  一般用于释放视图相关资源的操作
     */
    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView: ")
    }

    /**
     *  当Fragment被销毁时调用
     *  一般用于资源的释放
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ")
    }

    /**
     *  当Fragment与Activity接触关联时调用
     *  一般用于清理Fragment和Activity之间的关联操作
     */
    override fun onDetach() {
        super.onDetach()
        Log.e(TAG, "onDetach: ")
    }

    /**
     * @Author: ContentMy
     * @return: 返回布局ID e.g:R.layout.fragment_main
     * @Description: 子类实现抽象方法，返回布局id，用于在基类中统一设置布局
     */
    abstract fun getLayoutId(): Int

    /**
     * @Author: ContentMy
     * @Description: 初始化view相关的操作，使用open而不是静态函数是为了让子类实现有更多的操作空间
     */
    open fun initView(){

    }

    /**
     * @Author: ContentMy
     * @Description: 初始化数据相关的操作
     */
    open fun initData(){

    }

    /**
     * @Author: ContentMy
     * @Description: 初始化监听相关的操作
     */
    open fun initListener(){

    }

    /**
     * @Author: ContentMy
     * @Description:
     * 为了解决在BaseFragment和BaseMvvm中方法执行优先级的问题。把init相关的方法下沉到了最底层的base基类中，但在mvvm封装层中子类去实现绑定viewmodel的操作中，
     * 由于时序性导致了init函数实现要比viewmodel的绑定快，所以特此增加一个公开函数到base基类中，来解决这个问题
     *
     */
    open fun beforeInit(){

    }
}