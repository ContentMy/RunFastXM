package com.existmg.library_ui.views.countdown

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.existmg.library_ui.R
import com.existmg.library_ui.databinding.UiViewSimpleClockBinding
import java.util.concurrent.TimeUnit

/**
 * @Author ContentMy
 * @Date 2024/4/16 3:41 PM
 * @Description 这里是最终的封装的倒计时控件
 */
class CountDownClock : LinearLayout {
    private var countDownTimer: CountDownTimer? = null //倒计时器
    private var countdownListener: CountdownCallBack? = null //倒计时监听
    private var binding: UiViewSimpleClockBinding
    private var countdownTickInterval = 1000 //倒计时间隔

    private var almostFinishedCallbackTimeInSeconds: Int = 5

    private var resetSymbol: String = "0" //默认或者重置情况下的UI展示

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        binding = UiViewSimpleClockBinding.inflate(LayoutInflater.from(context),this,true)
        attrs?.let {
            //解析 XML 属性集合，获取自定义属性
            val typedArray =
                context?.obtainStyledAttributes(attrs, R.styleable.ui_CountDownClock, defStyleAttr, 0)
            val resetSymbol = typedArray?.getString(R.styleable.ui_CountDownClock_ui_resetSymbol)
            if (resetSymbol != null) {
                setResetSymbol(resetSymbol)
            }

            val digitTopDrawable =
                typedArray?.getDrawable(R.styleable.ui_CountDownClock_ui_digitTopDrawable)
            setDigitTopDrawable(digitTopDrawable)
            val digitBottomDrawable =
                typedArray?.getDrawable(R.styleable.ui_CountDownClock_ui_digitBottomDrawable)
            setDigitBottomDrawable(digitBottomDrawable)
            val digitDividerColor =
                typedArray?.getColor(R.styleable.ui_CountDownClock_ui_digitDividerColor, 0)
            setDigitDividerColor(digitDividerColor ?: 0)
            val digitSplitterColor =
                typedArray?.getColor(R.styleable.ui_CountDownClock_ui_digitSplitterColor, 0)
            setDigitSplitterColor(digitSplitterColor ?: 0)

            val digitTextColor = typedArray?.getColor(R.styleable.ui_CountDownClock_ui_digitTextColor, 0)
            setDigitTextColor(digitTextColor ?: 0)

            val digitTextSize =
                typedArray?.getDimension(R.styleable.ui_CountDownClock_ui_digitTextSize, 0f)
            setDigitTextSize(digitTextSize ?: 0f)
            setSplitterDigitTextSize(digitTextSize ?: 0f)

            val digitPadding = typedArray?.getDimension(R.styleable.ui_CountDownClock_ui_digitPadding, 0f)
            setDigitPadding(digitPadding?.toInt() ?: 0)

            val splitterPadding =
                typedArray?.getDimension(R.styleable.ui_CountDownClock_ui_splitterPadding, 0f)
            setSplitterPadding(splitterPadding?.toInt() ?: 0)

            val halfDigitHeight =
                typedArray?.getDimensionPixelSize(R.styleable.ui_CountDownClock_ui_halfDigitHeight, 0)
            val digitWidth =
                typedArray?.getDimensionPixelSize(R.styleable.ui_CountDownClock_ui_digitWidth, 0)
            setHalfDigitHeightAndDigitWidth(halfDigitHeight ?: 0, digitWidth ?: 0)

            val animationDuration =
                typedArray?.getInt(R.styleable.ui_CountDownClock_ui_animationDuration, 0)
            setAnimationDuration(animationDuration ?: 600)

            val almostFinishedCallbackTimeInSeconds = typedArray?.getInt(
                R.styleable.ui_CountDownClock_ui_almostFinishedCallbackTimeInSeconds,
                5
            )
            setAlmostFinishedCallbackTimeInSeconds(almostFinishedCallbackTimeInSeconds ?: 5)

            val countdownTickInterval =
                typedArray?.getInt(R.styleable.ui_CountDownClock_ui_countdownTickInterval, 1000)
            this.countdownTickInterval = countdownTickInterval ?: 1000

            invalidate()//刷新或重新绘制UI
            typedArray?.recycle()//使用完 TypedArray 对象后及时释放其资源，以确保内存的正确管理
        }
    }

    ////////////////
    // Public methods
    ////////////////

    private var milliLeft: Long = 0 //剩余时间

    //执行倒计时功能
    fun startCountDown(timeToNextEvent: Long) {
        countDownTimer?.cancel()//用于取消之前可能存在的倒计时器
        var hasCalledAlmostFinished = false
        countDownTimer = object : CountDownTimer(timeToNextEvent, countdownTickInterval.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                //在onTick回调中处理每个间隔的操作
                milliLeft = millisUntilFinished//更新剩余时间
                if (millisUntilFinished / 1000 <= almostFinishedCallbackTimeInSeconds && !hasCalledAlmostFinished) {
                    //逻辑判断：倒计时是否接近结束，通过接近结束的时间参数设置以及布尔值来确定
                    hasCalledAlmostFinished = true
                    countdownListener?.countdownAboutToFinish()//调用提供给开发者的即将结束回调，方便开发者进行一些UI判断和展示
                }
                //更新UI展示
                setCountDownTime(millisUntilFinished)
            }

            override fun onFinish() {
                hasCalledAlmostFinished = false
                countdownListener?.countdownFinished()//调用提供给开发者的结束回调，方便开发者进行一些UI判断和展示
            }
        }
        countDownTimer?.start()//开启倒计时器
    }

    //重置倒计时器，置为0
    fun resetCountdownTimer() {
        countDownTimer?.cancel()
        binding.firstDigitDays.setNewText(resetSymbol)
        binding.secondDigitDays.setNewText(resetSymbol)
        binding.firstDigitHours.setNewText(resetSymbol)
        binding.secondDigitHours.setNewText(resetSymbol)
        binding.firstDigitMinute.setNewText(resetSymbol)
        binding.secondDigitMinute.setNewText(resetSymbol)
        binding.firstDigitSecond.setNewText(resetSymbol)
        binding.secondDigitSecond.setNewText(resetSymbol)
    }

    ////////////////
    // Private methods
    ////////////////

    /**
     * @Author: ContentMy
     * @params: timeToStart
     * @Description: 这里是设置时间文本的方法，通过对给定的时间单位进行格式处理得到对应的时间文本并设置到控件中
     */
    private fun setCountDownTime(timeToStart: Long) {

        val days = TimeUnit.MILLISECONDS.toDays(timeToStart)
        val hours = TimeUnit.MILLISECONDS.toHours(timeToStart - TimeUnit.DAYS.toMillis(days))
        val minutes = TimeUnit.MILLISECONDS.toMinutes(
            timeToStart - (TimeUnit.DAYS.toMillis(days) + TimeUnit.HOURS.toMillis(hours))
        )
        val seconds = TimeUnit.MILLISECONDS.toSeconds(
            timeToStart - (TimeUnit.DAYS.toMillis(days) + TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(
                minutes
            ))
        )

        val daysString = days.toString()
        val hoursString = hours.toString()
        val minutesString = minutes.toString()
        val secondsString = seconds.toString()


        when {
            daysString.length == 2 -> {
                binding.firstDigitDays.animateTextChange((daysString[0].toString()))
                binding.secondDigitDays.animateTextChange((daysString[1].toString()))
            }
            daysString.length == 1 -> {
                binding.firstDigitDays.animateTextChange(("0"))
                binding.secondDigitDays.animateTextChange((daysString[0].toString()))
            }
            else -> {
                binding.firstDigitDays.animateTextChange(("3"))
                binding.secondDigitDays.animateTextChange(("0"))
            }
        }

        when {
            hoursString.length == 2 -> {
                binding.firstDigitHours.animateTextChange((hoursString[0].toString()))
                binding.secondDigitHours.animateTextChange((hoursString[1].toString()))
            }
            hoursString.length == 1 -> {
                binding.firstDigitHours.animateTextChange(("0"))
                binding.secondDigitHours.animateTextChange((hoursString[0].toString()))
            }
            else -> {
                binding.firstDigitHours.animateTextChange(("1"))
                binding.secondDigitHours.animateTextChange(("1"))
            }
        }

        when {
            minutesString.length == 2 -> {
                binding.firstDigitMinute.animateTextChange((minutesString[0].toString()))
                binding.secondDigitMinute.animateTextChange((minutesString[1].toString()))
            }
            minutesString.length == 1 -> {
                binding.firstDigitMinute.animateTextChange(("0"))
                binding.secondDigitMinute.animateTextChange((minutesString[0].toString()))
            }
            else -> {
                binding.firstDigitMinute.animateTextChange(("5"))
                binding.secondDigitMinute.animateTextChange(("9"))
            }
        }
        when {
            secondsString.length == 2 -> {
                binding.firstDigitSecond.animateTextChange((secondsString[0].toString()))
                binding.secondDigitSecond.animateTextChange((secondsString[1].toString()))
            }
            secondsString.length == 1 -> {
                binding.firstDigitSecond.animateTextChange(("0"))
                binding.secondDigitSecond.animateTextChange((secondsString[0].toString()))
            }
            else -> {
                binding.firstDigitSecond.animateTextChange((secondsString[secondsString.length - 2].toString()))
                binding.secondDigitSecond.animateTextChange((secondsString[secondsString.length - 1].toString()))
            }
        }

        if (days > 0) {
            binding.firstDigitDays.visibility = VISIBLE
            binding.secondDigitDays.visibility = VISIBLE
            binding.labelDays.visibility = VISIBLE
        } else {
            binding.firstDigitDays.visibility = GONE
            binding.secondDigitDays.visibility = GONE
            binding.labelDays.visibility = GONE
        }

        if (hours <= 0 && days <=0) {
            binding.firstDigitHours.visibility = GONE
            binding.secondDigitHours.visibility = GONE
            binding.labelHours.visibility = GONE
        } else {
            binding.firstDigitHours.visibility = VISIBLE
            binding.secondDigitHours.visibility = VISIBLE
            binding.labelHours.visibility = VISIBLE
        }

        if (minutes <= 0 && hours<=0 && days<=0) {
            binding.firstDigitMinute.visibility = GONE
            binding.secondDigitMinute.visibility = GONE
            binding.labelMinute.visibility = GONE
        } else {
            binding.firstDigitMinute.visibility = VISIBLE
            binding.secondDigitMinute.visibility = VISIBLE
            binding.labelMinute.visibility = VISIBLE
        }

        if (seconds <= 0 && minutes<=0 && hours<=0 && days<=0) {
            binding.firstDigitSecond.visibility = GONE
            binding.secondDigitSecond.visibility = GONE
            binding.labelSecond.visibility = GONE
        } else {
            binding.firstDigitSecond.visibility = VISIBLE
            binding.secondDigitSecond.visibility = VISIBLE
            binding.labelSecond.visibility = VISIBLE
        }
    }

    private fun setResetSymbol(resetSymbol: String?) {
        resetSymbol?.let {
            if (it.isNotEmpty()) {
                this.resetSymbol = resetSymbol
            } else {
                this.resetSymbol = ""
            }
        } ?: kotlin.run {
            this.resetSymbol = ""
        }
    }

    private fun setDigitTopDrawable(digitTopDrawable: Drawable?) {
        if (digitTopDrawable != null) {
            binding.firstDigitDays.getFrontUpper().background = digitTopDrawable
            binding.firstDigitDays.getBackUpper().background = digitTopDrawable
            binding.secondDigitDays.getFrontUpper().background = digitTopDrawable
            binding.secondDigitDays.getBackUpper().background = digitTopDrawable
            binding.firstDigitHours.getFrontUpper().background = digitTopDrawable
            binding.firstDigitHours.getBackUpper().background = digitTopDrawable
            binding.secondDigitHours.getFrontUpper().background = digitTopDrawable
            binding.secondDigitHours.getBackUpper().background = digitTopDrawable
            binding.firstDigitMinute.getFrontUpper().background = digitTopDrawable
            binding.firstDigitMinute.getBackUpper().background = digitTopDrawable
            binding.secondDigitMinute.getFrontUpper().background = digitTopDrawable
            binding.secondDigitMinute.getBackUpper().background = digitTopDrawable
            binding.firstDigitSecond.getFrontUpper().background = digitTopDrawable
            binding.firstDigitSecond.getBackUpper().background = digitTopDrawable
            binding.secondDigitSecond.getFrontUpper().background = digitTopDrawable
            binding.secondDigitSecond.getBackUpper().background = digitTopDrawable
        } else {
            setTransparentBackgroundColor()
        }
    }

    private fun setDigitBottomDrawable(digitBottomDrawable: Drawable?) {
        if (digitBottomDrawable != null) {
            binding.firstDigitDays.getFrontLower().background = digitBottomDrawable
            binding.firstDigitDays.getBackLower().background = digitBottomDrawable
            binding.secondDigitDays.getFrontLower().background = digitBottomDrawable
            binding.secondDigitDays.getBackLower().background = digitBottomDrawable
            binding.firstDigitHours.getFrontLower().background = digitBottomDrawable
            binding.firstDigitHours.getBackLower().background = digitBottomDrawable
            binding.secondDigitHours.getFrontLower().background = digitBottomDrawable
            binding.secondDigitHours.getBackLower().background = digitBottomDrawable
            binding.firstDigitMinute.getFrontLower().background = digitBottomDrawable
            binding.firstDigitMinute.getBackLower().background = digitBottomDrawable
            binding.secondDigitMinute.getFrontLower().background = digitBottomDrawable
            binding.secondDigitMinute.getBackLower().background = digitBottomDrawable
            binding.firstDigitSecond.getFrontLower().background = digitBottomDrawable
            binding.firstDigitSecond.getBackLower().background = digitBottomDrawable
            binding.secondDigitSecond.getFrontLower().background = digitBottomDrawable
            binding.secondDigitSecond.getBackLower().background = digitBottomDrawable
        } else {
            setTransparentBackgroundColor()
        }
    }

    private fun setDigitDividerColor(digitDividerColor: Int) {
        var dividerColor = digitDividerColor
        if (dividerColor == 0) {
            dividerColor = ContextCompat.getColor(context, R.color.ui_transparent)
        }

        binding.firstDigitDays.getDigitDivider().setBackgroundColor(dividerColor)
        binding.secondDigitDays.getDigitDivider().setBackgroundColor(dividerColor)
        binding.firstDigitHours.getDigitDivider().setBackgroundColor(dividerColor)
        binding.secondDigitHours.getDigitDivider().setBackgroundColor(dividerColor)
        binding.firstDigitMinute.getDigitDivider().setBackgroundColor(dividerColor)
        binding.secondDigitMinute.getDigitDivider().setBackgroundColor(dividerColor)
        binding.firstDigitSecond.getDigitDivider().setBackgroundColor(dividerColor)
        binding.secondDigitSecond.getDigitDivider().setBackgroundColor(dividerColor)
    }

    private fun setDigitSplitterColor(digitsSplitterColor: Int) {
        if (digitsSplitterColor != 0) {
            //  digitsSplitter.setTextColor(digitsSplitterColor)
        } else {
            // digitsSplitter.setTextColor(ContextCompat.getColor(context, R.color.transparent))
        }
    }

    private fun setSplitterDigitTextSize(digitsTextSize: Float) {
        //digitsSplitter.setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
    }

    private fun setDigitPadding(digitPadding: Int) {

        binding.firstDigitDays.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
        binding.secondDigitDays.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
        binding.firstDigitHours.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
        binding.secondDigitHours.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
        binding.firstDigitMinute.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
        binding.secondDigitMinute.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
        binding.firstDigitSecond.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
        binding.secondDigitSecond.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
    }

    private fun setSplitterPadding(splitterPadding: Int) {
        //digitsSplitter.setPadding(splitterPadding, 0, splitterPadding, 0)
    }

    private fun setDigitTextColor(digitsTextColor: Int) {
        var textColor = digitsTextColor
        if (textColor == 0) {
            textColor = ContextCompat.getColor(context, R.color.ui_transparent)
        }

        binding.firstDigitDays.getFrontUpperText().setTextColor(textColor)
        binding.firstDigitDays.getBackUpperText().setTextColor(textColor)
        binding.firstDigitHours.getFrontUpperText().setTextColor(textColor)
        binding.firstDigitHours.getBackUpperText().setTextColor(textColor)
        binding.secondDigitDays.getFrontUpperText().setTextColor(textColor)
        binding.secondDigitDays.getBackUpperText().setTextColor(textColor)
        binding.secondDigitHours.getFrontUpperText().setTextColor(textColor)
        binding.secondDigitHours.getBackUpperText().setTextColor(textColor)
        binding.firstDigitMinute.getFrontUpperText().setTextColor(textColor)
        binding.firstDigitMinute.getBackUpperText().setTextColor(textColor)
        binding.secondDigitMinute.getFrontUpperText().setTextColor(textColor)
        binding.secondDigitMinute.getBackUpperText().setTextColor(textColor)
        binding.firstDigitSecond.getFrontUpperText().setTextColor(textColor)
        binding.firstDigitSecond.getBackUpperText().setTextColor(textColor)
        binding.secondDigitSecond.getFrontUpperText().setTextColor(textColor)
        binding.secondDigitSecond.getBackUpperText().setTextColor(textColor)


        binding.firstDigitDays.getFrontLowerText().setTextColor(textColor)
        binding.firstDigitDays.getBackLowerText().setTextColor(textColor)
        binding.firstDigitHours.getFrontLowerText().setTextColor(textColor)
        binding.firstDigitHours.getBackLowerText().setTextColor(textColor)
        binding.secondDigitDays.getFrontLowerText().setTextColor(textColor)
        binding.secondDigitDays.getBackLowerText().setTextColor(textColor)

        binding.secondDigitHours.getFrontLowerText().setTextColor(textColor)
        binding.secondDigitHours.getBackLowerText().setTextColor(textColor)
        binding.firstDigitMinute.getFrontLowerText().setTextColor(textColor)
        binding.firstDigitMinute.getBackLowerText().setTextColor(textColor)
        binding.secondDigitMinute.getFrontLowerText().setTextColor(textColor)
        binding.secondDigitMinute.getBackLowerText().setTextColor(textColor)
        binding.firstDigitSecond.getFrontLowerText().setTextColor(textColor)
        binding.firstDigitSecond.getBackLowerText().setTextColor(textColor)
        binding.secondDigitSecond.getFrontLowerText().setTextColor(textColor)
        binding.secondDigitSecond.getBackLowerText().setTextColor(textColor)
    }

    private fun setDigitTextSize(digitsTextSize: Float) {

        binding.firstDigitDays.getFrontUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitDays.getBackUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitDays.getFrontUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitDays.getBackUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitHours.getFrontUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitHours.getBackUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitHours.getFrontUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitHours.getBackUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitMinute.getFrontUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitMinute.getBackUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitMinute.getFrontUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitMinute.getBackUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitSecond.getFrontUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitSecond.getBackUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitSecond.getFrontUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitSecond.getBackUpperText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitDays.getFrontLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitDays.getBackLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitDays.getFrontLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitDays.getBackLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitHours.getFrontLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitHours.getBackLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitHours.getFrontLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitHours.getBackLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitMinute.getFrontLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitMinute.getBackLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitMinute.getFrontLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitMinute.getBackLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitSecond.getFrontLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.firstDigitSecond.getBackLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitSecond.getFrontLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
        binding.secondDigitSecond.getBackLowerText().setTextSize(TypedValue.COMPLEX_UNIT_PX, digitsTextSize)
    }

    private fun setHalfDigitHeightAndDigitWidth(halfDigitHeight: Int, digitWidth: Int) {
        setHeightAndWidthToView(binding.firstDigitDays.getFrontUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.firstDigitDays.getBackUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitDays.getFrontUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitDays.getBackUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.firstDigitHours.getFrontUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.firstDigitHours.getBackUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitHours.getFrontUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitHours.getBackUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.firstDigitMinute.getFrontUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.firstDigitMinute.getBackUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitMinute.getFrontUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitMinute.getBackUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.firstDigitSecond.getFrontUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.firstDigitSecond.getBackUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitSecond.getFrontUpper(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitSecond.getBackUpper(), halfDigitHeight, digitWidth)

        // Lower
        setHeightAndWidthToView(binding.firstDigitDays.getFrontLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.firstDigitDays.getBackLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitDays.getFrontLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitDays.getBackLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.firstDigitHours.getFrontLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.firstDigitHours.getBackLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitHours.getFrontLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitHours.getBackLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.firstDigitMinute.getFrontLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.firstDigitMinute.getBackLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitMinute.getFrontLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitMinute.getBackLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.firstDigitSecond.getFrontLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.firstDigitSecond.getBackLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitSecond.getFrontLower(), halfDigitHeight, digitWidth)
        setHeightAndWidthToView(binding.secondDigitSecond.getBackLower(), halfDigitHeight, digitWidth)

        // Dividers
        binding.firstDigitDays.getDigitDivider().layoutParams.width = digitWidth
        binding.secondDigitDays.getDigitDivider().layoutParams.width = digitWidth
        binding.firstDigitHours.getDigitDivider().layoutParams.width = digitWidth
        binding.secondDigitHours.getDigitDivider().layoutParams.width = digitWidth
        binding.firstDigitMinute.getDigitDivider().layoutParams.width = digitWidth
        binding.secondDigitMinute.getDigitDivider().layoutParams.width = digitWidth
        binding.firstDigitSecond.getDigitDivider().layoutParams.width = digitWidth
        binding.secondDigitSecond.getDigitDivider().layoutParams.width = digitWidth
    }

    private fun setHeightAndWidthToView(view: View, halfDigitHeight: Int, digitWidth: Int) {
        val firstDigitMinuteFrontUpperLayoutParams = view.layoutParams
        firstDigitMinuteFrontUpperLayoutParams.height = halfDigitHeight
        firstDigitMinuteFrontUpperLayoutParams.width = digitWidth
        binding.firstDigitDays.getFrontUpper().layoutParams = firstDigitMinuteFrontUpperLayoutParams
    }

    private fun setAnimationDuration(animationDuration: Int) {
        binding.firstDigitDays.setAnimationDuration(animationDuration.toLong())
        binding.secondDigitDays.setAnimationDuration(animationDuration.toLong())
        binding.firstDigitHours.setAnimationDuration(animationDuration.toLong())
        binding.secondDigitHours.setAnimationDuration(animationDuration.toLong())
        binding.firstDigitMinute.setAnimationDuration(animationDuration.toLong())
        binding.secondDigitMinute.setAnimationDuration(animationDuration.toLong())
        binding.firstDigitSecond.setAnimationDuration(animationDuration.toLong())
        binding.secondDigitSecond.setAnimationDuration(animationDuration.toLong())
    }

    private fun setAlmostFinishedCallbackTimeInSeconds(almostFinishedCallbackTimeInSeconds: Int) {
        this.almostFinishedCallbackTimeInSeconds = almostFinishedCallbackTimeInSeconds
    }

    private fun setTransparentBackgroundColor() {
        val transparent = ContextCompat.getColor(context, R.color.ui_transparent)
        binding.firstDigitDays.getFrontLower().setBackgroundColor(transparent)
        binding.firstDigitDays.getBackLower().setBackgroundColor(transparent)
        binding.secondDigitDays.getFrontLower().setBackgroundColor(transparent)
        binding.secondDigitDays.getBackLower().setBackgroundColor(transparent)
        binding.firstDigitHours.getFrontLower().setBackgroundColor(transparent)
        binding.firstDigitHours.getBackLower().setBackgroundColor(transparent)
        binding.secondDigitHours.getFrontLower().setBackgroundColor(transparent)
        binding.secondDigitHours.getBackLower().setBackgroundColor(transparent)
        binding.firstDigitMinute.getFrontLower().setBackgroundColor(transparent)
        binding.firstDigitMinute.getBackLower().setBackgroundColor(transparent)
        binding.secondDigitMinute.getFrontLower().setBackgroundColor(transparent)
        binding.secondDigitMinute.getBackLower().setBackgroundColor(transparent)
        binding.firstDigitSecond.getFrontLower().setBackgroundColor(transparent)
        binding.firstDigitSecond.getBackLower().setBackgroundColor(transparent)
        binding.secondDigitSecond.getFrontLower().setBackgroundColor(transparent)
        binding.secondDigitSecond.getBackLower().setBackgroundColor(transparent)
    }

    ////////////////
    // Listeners
    ////////////////

    fun setCountdownListener(countdownListener: CountdownCallBack) {
        this.countdownListener = countdownListener
    }

    interface CountdownCallBack {
        fun countdownAboutToFinish()
        fun countdownFinished()
    }


    fun pauseCountDownTimer() {
        countDownTimer?.cancel()
    }

    fun resumeCountDownTimer() {
        startCountDown(milliLeft)
    }


    fun setCustomTypeface(typeface: Typeface) {
        binding.firstDigitDays.setTypeFace(typeface)
        binding.firstDigitDays.setTypeFace(typeface)
        binding.secondDigitDays.setTypeFace(typeface)
        binding.secondDigitDays.setTypeFace(typeface)
        binding.firstDigitHours.setTypeFace(typeface)
        binding.firstDigitHours.setTypeFace(typeface)
        binding.secondDigitHours.setTypeFace(typeface)
        binding.secondDigitHours.setTypeFace(typeface)
        binding.firstDigitMinute.setTypeFace(typeface)
        binding.firstDigitMinute.setTypeFace(typeface)
        binding.secondDigitMinute.setTypeFace(typeface)
        binding.secondDigitMinute.setTypeFace(typeface)
        binding.firstDigitSecond.setTypeFace(typeface)
        binding.firstDigitSecond.setTypeFace(typeface)
        binding.secondDigitSecond.setTypeFace(typeface)
        binding.secondDigitSecond.setTypeFace(typeface)

    }


}