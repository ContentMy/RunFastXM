package com.existmg.module_memorandum.ui.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Path
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.PathInterpolator
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.existmg.library_common.interfaces.OnItemClickListener
import com.existmg.library_common.interfaces.OnItemLongClickListener
import com.existmg.library_common.utils.timeLongToString
import com.existmg.library_data.db.entity.MemorandumEntity
import com.existmg.module_memorandum.R
import com.existmg.module_memorandum.databinding.MemorandumLayoutItemMemorandumBinding
import com.existmg.module_memorandum.interfaces.MemorandumItemDeleteCallback
import kotlin.random.Random


/**
 * @Author ContentMy
 * @Date 2024/4/28 2:00 PM
 * @Description
 */
class MemorandumRecycleViewAdapter(private var list:MutableList<MemorandumEntity>):
    RecyclerView.Adapter<MemorandumRecycleViewAdapter.MemorandumDataBindingViewHolder>(){
    private var mIsShaking = false
    private var isExistDelete = false
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private var mMemorandumItemDeleteCallback:MemorandumItemDeleteCallback? = null
    // 存储每个项目的删除按钮可见性状态
    private var deleteButtonVisibleList: MutableList<Boolean>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemorandumDataBindingViewHolder {
        val view = DataBindingUtil.inflate<MemorandumLayoutItemMemorandumBinding>(
            LayoutInflater.from(parent.context),
            R.layout.memorandum_layout_item_memorandum,parent,false);
        return MemorandumDataBindingViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MemorandumDataBindingViewHolder,
        position: Int
    ) {
        val item = list[position]
        holder.dataBinding.memorandumItemTvDate.text = timeLongToString(item.memorandumCreateTime)
        holder.dataBinding.memorandumItemTvTitle.text = item.memorandumTitle
        holder.dataBinding.memorandumItemTvContent.text = item.memorandumContent
        if (!deleteButtonVisibleList.isNullOrEmpty()){
            holder.dataBinding.memorandumItemIvDelete.visibility = if (deleteButtonVisibleList!![position]) View.VISIBLE else View.GONE
        }
        holder.dataBinding.memorandumItemIvDelete.setOnClickListener {
            mMemorandumItemDeleteCallback?.itemDelete(item,position)
            list.remove(item)
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener {
            // 如果删除按钮可见，则隐藏删除按钮，并返回
            if (isExistDelete){//为什么不在内部处理或者说内部为什么还有isExistDelete的判断是因为，点击事件这里要单独处理一次return
                hideDeleteButton()
                return@setOnClickListener
            }
            mOnItemClickListener?.onItemClick(it,position)
        }

        holder.itemView.setOnLongClickListener {
            if (mOnItemLongClickListener != null){
                mOnItemLongClickListener?.onItemLongClick(it,position)
                deleteButtonVisibleList!![position] = true//长按item显示删除按钮
                isExistDelete = true
                notifyDataSetChanged()
                return@setOnLongClickListener true
            }
            return@setOnLongClickListener false
        }
        // 添加摇动效果
//        if (mIsShaking) {
//            holder.startShakeAnimation();
//        } else {
//            holder.stopShakeAnimation();
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setShaking(isShaking:Boolean){
        mIsShaking = isShaking
        notifyDataSetChanged()
    }

    fun setListDeleteStatus(listSize:Int){
        //暂定每次刷新数据时都同步重置这个list的数据
        deleteButtonVisibleList = MutableList(listSize) { false }
        notifyDataSetChanged()
    }

    fun hideDeleteButton() {
        if (deleteButtonVisibleList.isNullOrEmpty() || !isExistDelete){
            return
        }
        for (i in deleteButtonVisibleList?.indices!!) {
            deleteButtonVisibleList!![i] = false
        }
        isExistDelete = false
        notifyDataSetChanged()
    }
    //创建Holder
    class MemorandumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTv: TextView = itemView.findViewById(R.id.memorandum_item_tv_title)
    }
    //创建使用DataBinding的Holder
    class MemorandumDataBindingViewHolder(itemDataBinding:MemorandumLayoutItemMemorandumBinding):RecyclerView.ViewHolder(itemDataBinding.root){
        var dataBinding:MemorandumLayoutItemMemorandumBinding = itemDataBinding
        fun startShakeAnimation() {
            val path = Path().apply {
                moveTo(0f, 0f)
                cubicTo(0.2f, 1f, 0.8f, 0f, 1f, 1f)
            }
            val interpolator = PathInterpolator(path)
            val animatorX = ObjectAnimator.ofFloat(itemView, "translationX", (Random.nextInt(20) - 10).toFloat())
            val animatorY = ObjectAnimator.ofFloat(itemView, "translationY", (Random.nextInt(20) - 10).toFloat())
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(animatorX, animatorY)
            animatorSet.duration = 400
            animatorSet.interpolator = interpolator
            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    animation.startDelay = 0
                    animation.start()
                }
            })
            animatorSet.start()
        }

        fun stopShakeAnimation() {
            itemView.clearAnimation()
        }

    }


    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.mOnItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        this.mOnItemLongClickListener = listener
    }

    fun setMemorandumItemDeleteCallback(callback: MemorandumItemDeleteCallback){
        this.mMemorandumItemDeleteCallback = callback
    }
}