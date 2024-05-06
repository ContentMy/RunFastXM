package com.existmg.library_ui.dialog.base

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment

/**
 * @Author ContentMy
 * @Date 2024/4/13 11:32 PM
 * @Description
 * 适用场景：适合用于创建复杂的对话框，例如包含多个控件或需要与主界面交互的对话框。DialogFragment 可以像 Fragment 一样管理生命周期，并且可以与 Activity 或其他 Fragment 进行交互。适用于对话框内容较复杂，需要与其他界面交互的情况。
 * 优点：灵活性高，可以与其他 Fragment 进行交互，支持复杂的对话框内容和交互逻辑。
 * 缺点：稍微复杂一些，需要处理生命周期和与其他 Fragment 的通信。
 */
abstract class DialogBaseFragment : AppCompatDialogFragment() {
    private var defaultWIDTH = WindowManager.LayoutParams.MATCH_PARENT//宽
    private var defaultHEIGHT = WindowManager.LayoutParams.WRAP_CONTENT//高
    private var defaultGRAVITY = Gravity.BOTTOM//位置
    private var mCancelable = true//默认可取消
    private var mCanceledOnTouchOutside = true//默认点击外部可取消

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(getLayoutId(), container, false);
        initViews(mView);
        return mView;
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mDialog =  super.onCreateDialog(savedInstanceState)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside)
        mDialog.setCancelable(mCancelable)
        val window = mDialog.window
        if (null != window) {
            window.decorView.setPadding(0, 0, 0, 0);
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            val lp = window.attributes;
            lp.width = defaultWIDTH;
            lp.height = defaultHEIGHT;
            lp.gravity = defaultGRAVITY;
            lp.windowAnimations = android.R.style.Animation_InputMethod;
            window.setAttributes(lp);
        }
        mDialog.setOnKeyListener(object : DialogInterface.OnKeyListener{
            override fun onKey(
                dialog: DialogInterface?,
                keyCode: Int,
                event: KeyEvent?
            ): Boolean {
                return !mCancelable;
            }

        } );
        return mDialog;
    }
    /**
     * 设置位置
     * @param gravity
     */
    fun setGravity(gravity:Int) {
        defaultGRAVITY = gravity;
    }
    /**
     * 设置宽
     * @param width
     */
    fun setWidth(width:Int) {
        defaultWIDTH = width;
    }
    /**
     * 设置高
     * @param height
     */
    fun setHeight(height:Int) {
        defaultHEIGHT = height;
    }
    /**
     * 设置点击返回按钮是否可取消
     *
     * @param cancelable
     */
    override fun setCancelable(cancelable:Boolean) {
        mCancelable = cancelable;
    }
    /**
     * 设置点击外部是否可取消
     *
     * @param canceledOnTouchOutside
     */
    fun setCanceledOnTouchOutside(canceledOnTouchOutside:Boolean) {
        mCanceledOnTouchOutside = canceledOnTouchOutside;
    }
    /**
     * 设置布局
     *
     * @return
     */
    abstract fun getLayoutId():Int
    /**
     * 初始化Views
     *
     * @param v
     */
    abstract fun initViews(v:View);
}