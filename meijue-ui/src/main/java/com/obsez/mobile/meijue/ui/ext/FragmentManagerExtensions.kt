package com.obsez.mobile.meijue.ui.ext

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.obsez.mobile.meijue.ui.R

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

inline fun FragmentManager.inTransactionAdv(func: (ft: FragmentTransaction) -> Unit) {
    val ft = beginTransaction()
    func(ft)
    ft.commit()
}

fun FragmentTransaction.useDefaultFragmentAnimations() {
    // https://stackoverflow.com/questions/4932462/animate-the-transition-between-fragments/33992609
    // https://stackoverflow.com/questions/4817900/android-fragments-and-animation
    this.setCustomAnimations(R.anim.fr_enter, R.anim.fr_leave, R.anim.pop_enter, R.anim.pop_leave)
}


fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, tag: String? = null, addToBackStack: Boolean = false, func: (ft: FragmentTransaction) -> Unit) {
    supportFragmentManager.inTransactionAdv { ft ->
        func(ft)
        ft.add(frameId, fragment)
        if (!tag.isNullOrBlank() || addToBackStack)
            ft.addToBackStack(tag)
    }
}


fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, tag: String? = null, addToBackStack: Boolean = false, func: (ft: FragmentTransaction) -> Unit) {
    //    supportFragmentManager.inTransaction {
    //        replace(frameId, fragment)
    //    }
    supportFragmentManager.inTransactionAdv { ft ->
        func(ft)
        ft.replace(frameId, fragment)
        if (!tag.isNullOrBlank() || addToBackStack)
            ft.addToBackStack(tag)
    }
}



