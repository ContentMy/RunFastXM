package com.existmg.library_ui.views.countdown

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import com.existmg.library_ui.R

/**
 * @Author ContentMy
 * @Date 2024/4/16 3:38 PM
 * @Description 这是一个绘制文本的自定义view，根据ProperTextAlignment来分别绘制上半部分和下半部分
 */
class AlignedTextView : AppCompatTextView {
    private var alignment = ProperTextAlignment.TOP
    private val textRect = Rect()//Rect 类表示一个矩形，它由左上角的 (left, top) 和右下角的 (right, bottom) 构成

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.ui_AlignedTextView, defStyleAttr, 0)

            val alignment = typedArray.getInt(R.styleable.ui_AlignedTextView_ui_alignment, 0)
            if (alignment != null && alignment != 0) {
                setAlignment(alignment)
            } else {
                Log.e(
                    "AlignedTextView",
                    "You did not set an alignment for an AlignedTextView. Default is top alignment."
                )
            }

            invalidate()
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.let {
//            println("裁剪画布前 - 未偏移前textRect的bottom为：${textRect.bottom}，top为：${textRect.top},left为：${textRect.left},right为：${textRect.right}")
            canvas.getClipBounds(textRect)//获取当前画布的剪切边界
//            println("裁剪画布后 - 未偏移前textRect的bottom为：${textRect.bottom}，top为：${textRect.top},left为：${textRect.left},right为：${textRect.right}")
            val cHeight = textRect.height()//获取Rect的高度，如果此时给定自定义view控件的高度，那么是自定义view的控件高度，如果没有，那么是实际绘制时的裁剪区域的高度，比如绘制文本8，textSize 25，综合高度为81
//            println("textRect的高度为：$cHeight,文本的长度为：${this.text.length}")
//            println("获取文本前 - 未偏移前textRect的bottom为：${textRect.bottom}，top为：${textRect.top},left为：${textRect.left},right为：${textRect.right}")
            paint.getTextBounds(this.text.toString(), 0, this.text.length, textRect)//方法来获取指定文本在画布上绘制时所占据的矩形区域，并将结果存储在 textRect 对象中
            val bottom = textRect.bottom;//获取文本矩形区域的底部坐标
//            println("正式 - 未偏移前textRect的bottom为：${textRect.bottom}，top为：${textRect.top},left为：${textRect.left},right为：${textRect.right}")
            /*
            偏移解释：在 Rect 对象中，left 表示矩形左边缘与坐标系原点（通常是左上角）的水平距离，
            而 top 表示矩形上边缘与坐标系原点的垂直距离。因此，通过将这两个值设置为负值，
            可以将矩形的左上角移动到坐标系的原点处，即将矩形的坐标系偏移到原点
             */
            textRect.offset(-textRect.left, -textRect.top)//将文本矩形区域偏移至坐标原点
//            println("偏移后textRect的bottom为：${textRect.bottom}，top为：${textRect.top},left为：${textRect.left},right为：${textRect.right}")
            paint.textAlign = Paint.Align.CENTER//设置文本绘制时的对齐方式为居中对齐
            var drawY = 0f
            if (alignment == ProperTextAlignment.TOP) {//如果是上半部分，计算出绘制上半部分的y坐标
                /*
                通过计算偏移后的位置与
                */
                drawY = (textRect.bottom - bottom).toFloat() - ((textRect.bottom - textRect.top) / 2)
//                println("计算top时，bottom差值：${textRect.bottom - bottom},bottom与top差值：${textRect.bottom - textRect.top},实际的drawY：$drawY")
            } else if (alignment == ProperTextAlignment.BOTTOM) {//如果是下半部分，计算出绘制下半部分的y坐标
                drawY = top + cHeight.toFloat() + ((textRect.bottom - textRect.top) / 2)
//                println("计算bottom时，top+高度为：${cHeight.toFloat() + top},bottom与top差值：${textRect.bottom - textRect.top},实际的drawY：$drawY")
            }
            val drawX = (canvas.width / 2).toFloat()
            paint.color = this.currentTextColor
//            println("绘制时的X为：$drawX,Y为$drawY")
            canvas.drawText(this.text.toString(), drawX, drawY, paint)
        }
    }

    private fun setAlignment(alignment: Int) {
        if (alignment == 1) {
            this.alignment = ProperTextAlignment.TOP
        } else if (alignment == 2) {
            this.alignment = ProperTextAlignment.BOTTOM
        }
    }

}
private enum class ProperTextAlignment {
    TOP,
    BOTTOM
}