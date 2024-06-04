package com.existmg.module_remind.ui

import android.app.Application
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.existmg.library_common.fragment.BaseMvvmFragment
import com.existmg.library_common.managers.viewModelFactoryWithParams
import com.existmg.library_common.router.RouterFragmentPath
import com.existmg.library_data.accessor.RemindModuleRoomAccessor
import com.existmg.library_data.db.entity.RemindEntity
import com.existmg.library_data.repository.RemindRepository
import com.existmg.library_ui.dialog.interfaces.DialogDatabaseDelegate
import com.existmg.module_remind.R
import com.existmg.module_remind.databinding.RemindLayoutRemindFragmentBinding
import com.existmg.module_remind.databinding.RemindRecycleItemViewBinding
import com.existmg.module_remind.guide.RemindGuideActivity
import com.existmg.module_remind.ui.adapter.RemindRecycleAdapter
import com.existmg.module_remind.utils.logs.RemindLoggerManager
import com.existmg.module_remind.viewmodel.RemindViewModel

/**
 * @Author ContentMy
 * @Date 2024/4/15 10:55 PM
 * @Description 这里是倒计时提醒的列表页面，目前暂定为显示列表，功能如下：
 * 列表：
 * 用于展示所有倒计时提醒的数据，排列方式为提醒倒计时的时长递增
 * FAB:
 * 弹出一个dialog，不做视觉上的页面跳转
 * 点击列表Item
 * 跳转到倒计时页面，展示这个提醒的实时倒计时
 *
 */
@Route(path = RouterFragmentPath.Remind.PAGER_REMIND)
class RemindFragment : BaseMvvmFragment<RemindViewModel, RemindLayoutRemindFragmentBinding>(),DialogDatabaseDelegate,
    RemindRecycleAdapter.OnItemDeleteClickCallback {
    private val mLog = RemindLoggerManager.getLogger<RemindFragment>()
    private lateinit var adapter: RemindRecycleAdapter

    override fun getViewModelClass(): Class<RemindViewModel> {
        return RemindViewModel::class.java
    }

    override fun bindViewModel() {
        mBinding.viewModel = mViewModel
    }

    override fun provideViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactoryWithParams(RemindModuleRoomAccessor.getRemindRepository(),requireContext().applicationContext){
            RemindViewModel(it[0] as RemindRepository,it[1] as Application)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.remind_layout_remind_fragment
    }

    override fun initView() {
        adapter = RemindRecycleAdapter(null)
        val layoutManager = LinearLayoutManager(context)//需设置布局方向，否则不会展示数据
        mBinding.remindRv.layoutManager = layoutManager
        mBinding.remindRv.adapter = adapter
        mBinding.remindIncludeTitleToolbar.uiTitleToolbarIvRight.visibility = View.VISIBLE
        // 检查是否首次进入
        mViewModel.checkIfFirstTime("RemindGuideActivity")
        mViewModel.onOptimizationShow()
    }

    override fun initData() {
        mViewModel.refreshData()
        mBinding.remindIncludeTitleToolbar.uiTitleToolbarTvTitle.text = resources.getString(R.string.remind_module_list_name)
        mBinding.remindIncludeTitleToolbar.uiTitleToolbarIvRight.setImageResource(R.drawable.ui_more_menu)
    }

    override fun initListener() {
        adapter.setOnItemDeleteClickCallback(this)

        adapter.setOnItemClickListener { adapter, view, position ->
            val entity = adapter.data[position] as RemindEntity
            val intent = Intent(context, RemindDetailActivity::class.java)
            intent.putExtra("remindEntity", entity)
            startActivity(intent)//点击item时，把数据传递给activity
        }

        mBinding.remindIncludeTitleToolbar.uiTitleToolbarIvRight.setOnClickListener {
            startActivity(Intent(requireContext(),RemindCompletedActivity::class.java))
        }
    }

    override fun initObserver() {
        mViewModel.navigateToCreateTargetActivity.observe(viewLifecycleOwner, Observer {
            if (it){
//                displayDialogBottomFragment(childFragmentManager,this) TODO：暂时不使用dialogFragment的效果来完成了，有点击外部区域软键盘无法收回的问题，使用activity的dialog方案作为替代
                val intent = Intent(context, RemindCreateActivity::class.java)
                startActivity(intent)
            }
        })

        mViewModel.remindData.observe(viewLifecycleOwner){
            mBinding.remindTvDefaultContent.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            adapter.setList(it)
        }

        mViewModel.showGuide.observe(viewLifecycleOwner){
            if (it) {
                startActivity(Intent(requireContext(),RemindGuideActivity::class.java))
            }
        }

        mViewModel.optimizationShow.observe(viewLifecycleOwner){
            if (it){
                mBinding.remindOptimizationLl.visibility = View.VISIBLE
                mBinding.remindOptimizationIvClose.visibility = View.VISIBLE
            }
        }

        mViewModel.optimizationClose.observe(viewLifecycleOwner){
            if (it){
                mBinding.remindOptimizationLl.visibility = View.GONE
                mBinding.remindOptimizationIvClose.visibility = View.GONE
            }
        }

        mViewModel.navigateToOptimizationActivity.observe(viewLifecycleOwner){
            if (it){
                //使用路由跳转至user模块的UserRemindOptimizationActivity
                ARouter.getInstance().build(RouterFragmentPath.User.OPTIMIZATION_USER).navigation()
            }
        }
    }

    override fun processBusinessLogic(data: Map<String, Any>) {
        mLog.debug("创建数据")
//        mViewModel.insertData(data)
//        mViewModel.refreshData()//TODO:这里暂时调用了查询数据库的函数以更新数据来达到刷新ui列表的目的，后续考虑单个查询，或者不去考虑查询，直接在插入成功的回调中将数据返回进行ui更新
    }

    override fun onResume() {
        super.onResume()
        //TODO:尝试在回到Fragment时，如果键盘没有收回，手动收回键盘的方案。但是没有起到效果。考虑方案二，点击dialog外部不消失，必须手动点击或者取消。考虑方案三，使用activity实现，点击外部手动去finish
        hideSoftInput(mBinding.remindFab)
    }
    private fun hideSoftInput(v:View){//TODO: 统一封装一下软键盘的显示与隐藏
        if (v.windowToken != null)mLog.debug("${v.javaClass}")
        val manager = requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun itemDeleteClick(
        holder: BaseDataBindingHolder<RemindRecycleItemViewBinding>,
        position: Int,
        item: RemindEntity
    ) {
        mLog.debug("点击了删除按钮")
        mViewModel.deleteRemind(item)
    }
}