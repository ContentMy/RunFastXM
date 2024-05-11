package com.existmg.library_ui.views.countdown

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.existmg.library_ui.R
import com.existmg.library_ui.databinding.UiViewCountdownClockDigitBinding

/**
 * @Author ContentMy
 * @Date 2024/4/16 3:44 PM
 * @Description 这是对AlignedTextView封装的一个自定义View，组合成了一个时间控件的单个控件UI
 */

class CountDownDigit : FrameLayout {
    private var animationDuration = 600L
    private var binding: UiViewCountdownClockDigitBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.ui_view_countdown_clock_digit,
            this,
            true
        )

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        //对控件进行测量
        //0对应的SpecMode是UNSPECIFIED，可以理解为子元素不在父容器加以任何约束的情况下进行测量
        binding.frontUpperText.measure(0, 0)
        binding.frontLowerText.measure(0, 0)
        binding.backUpperText.measure(0, 0)
        binding.backLowerText.measure(0, 0)
    }
    
    /**
     * @Author: ContentMy
     * @param: newText 
     * @Description:
     */
    fun setNewText(newText: String) {
        binding.frontUpper.clearAnimation()
        binding.frontLower.clearAnimation()

        binding.frontUpperText.text = newText
        binding.frontLowerText.text = newText
        binding.backUpperText.text = newText
        binding.backLowerText.text = newText
    }

    /**
     * @Author: ContentMy
     * @params: newText
     * @Description: 
     */
    fun animateTextChange(newText: String) {
        if (binding.backUpperText.text == newText) {
            return
        }

        binding.frontUpper.clearAnimation()
        binding.frontLower.clearAnimation()

        binding.backUpperText.text = newText
        binding.frontUpper.pivotY = binding.frontUpper.bottom.toFloat()
        binding.frontLower.pivotY = binding.frontUpper.top.toFloat()
        binding.frontUpper.pivotX = (binding.frontUpper.right - ((binding.frontUpper.right - binding.frontUpper.left) / 2)).toFloat()
        binding.frontLower.pivotX = (binding.frontUpper.right - ((binding.frontUpper.right - binding.frontUpper.left) / 2)).toFloat()

        binding.frontUpper.animate().setDuration(getHalfOfAnimationDuration()).rotationX(-90f).setInterpolator(
            AccelerateInterpolator()
        )
            .withEndAction {
                binding.frontUpperText.text = binding.backUpperText.text
                binding.frontUpper.rotationX = 0f
                binding.frontLower.rotationX = 90f
                binding.frontLowerText.text = binding.backUpperText.text
                binding.frontLower.animate().setDuration(getHalfOfAnimationDuration()).rotationX(0f).setInterpolator(
                    DecelerateInterpolator()
                )
                    .withEndAction {
                        binding.backLowerText.text = binding.frontLowerText.text
                    }.start()
            }.start()
    }

    /**
     * @Author: ContentMy
     * @params: duration
     * @Description: 设置动画时长
     */
    fun setAnimationDuration(duration: Long) {
        this.animationDuration = duration
    }

    /**
     * @Author: ContentMy
     * @Description: 获取一半的时长用于计算
     */
    private fun getHalfOfAnimationDuration(): Long {
        return animationDuration / 2
    }

    /**
     * @Author: ContentMy
     * @params: typeFace
     * @Description: 设置控件中的view的字体类型，用来定义文本的字体样式、大小和其他属性
     */
    fun setTypeFace(typeFace: Typeface){

        binding.frontUpperText.typeface = typeFace
        binding.frontLowerText.typeface = typeFace
        binding.backUpperText.typeface = typeFace
        binding.backLowerText.typeface = typeFace

    }

    /**
     * @Author: ContentMy
     * @Description: 获取顶层上半部分的外层view(使用FrameLayout包裹了AlignedTextView，这里是指FrameLayout)，用于外层自定义view拿到view设置相关的属性
     */
    fun getFrontUpper(): View {
        return binding.frontUpper
    }

    /**
     * @Author: ContentMy
     * @Description: 获取顶层下半部分的view(使用FrameLayout包裹了AlignedTextView，这里是指FrameLayout)，用于外层自定义view拿到view设置相关的属性
     */
    fun getFrontLower(): View {
        return binding.frontLower
    }

    /**
     * @Author: ContentMy
     * @Description: 获取底层上半部分的view(使用FrameLayout包裹了AlignedTextView，这里是指FrameLayout)，用于外层自定义view拿到view设置相关的属性
     */
    fun getBackUpper(): View {
        return binding.backUpper
    }

    /**
     * @Author: ContentMy
     * @Description: 获取底层下半部分的view(使用FrameLayout包裹了AlignedTextView，这里是指FrameLayout)，用于外层自定义view拿到view设置相关的属性
     */
    fun getBackLower(): View {
        return binding.backLower
    }

    /**
     * @Author: ContentMy
     * @Description: 获取分割线的view，用于外层拿到view设置相关的属性
     */
    fun getDigitDivider(): View {
        return binding.digitDivider
    }

    /**
     * @Author: ContentMy
     * @Description: 获取顶层上半部分的view，用于外层自定义view拿到view设置相关的属性
     */
    fun getFrontUpperText(): AlignedTextView {
        return binding.frontUpperText
    }

    /**
     * @Author: ContentMy
     * @Description: 获取顶层下半部分的view，用于外层自定义view设置相关的属性
     */
    fun getFrontLowerText(): AlignedTextView {
        return binding.frontLowerText
    }

    /**
     * @Author: ContentMy
     * @Description: 获取底层上半部分的view，用于外层自定义view拿到view设置相关的属性
     */
    fun getBackUpperText(): AlignedTextView {
        return binding.backUpperText
    }

    /**
     * @Author: ContentMy
     * @Description: 获取底层下半部分的view，用于外层自定义view拿到view设置相关的属性
     */
    fun getBackLowerText(): AlignedTextView {
        return binding.backLowerText
    }

}