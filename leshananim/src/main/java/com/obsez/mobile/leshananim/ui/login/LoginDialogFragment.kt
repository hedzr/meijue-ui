package com.obsez.mobile.leshananim.ui.login

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import com.obsez.mobile.leshananim.R


class LoginBottomSheetDialogFragment : BottomSheetDialogFragment() {
    
    private var mBehavior: BottomSheetBehavior<View>? = null
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.login_bottom_sheet_dialog, null)
        dialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)
        return dialog
    }
    
    override fun onStart() {
        super.onStart()
        //默认全屏展开
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }
    
    fun doclick(_: View) {
        //点击任意布局关闭
        mBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
    }
}

