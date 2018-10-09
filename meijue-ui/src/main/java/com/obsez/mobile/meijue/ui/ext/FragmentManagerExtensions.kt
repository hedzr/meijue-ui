package com.obsez.mobile.meijue.ui.ext

import androidx.appcompat.app.AppCompatActivity
import com.obsez.mobile.meijue.ui.R

@Suppress("NOTHING_TO_INLINE")
inline fun androidx.fragment.app.FragmentManager.inTransaction(noinline func: (androidx.fragment.app.FragmentTransaction.() -> androidx.fragment.app.FragmentTransaction)? = null) {
    // beginTransaction().func().commit()
    val ft = beginTransaction()
    func?.invoke(ft)
    ft.commit()
}

@Suppress("NOTHING_TO_INLINE")
inline fun androidx.fragment.app.FragmentManager.inTransactionAdv(noinline func: ((ft: androidx.fragment.app.FragmentTransaction) -> Unit)? = null) {
    val ft = beginTransaction()
    func?.invoke(ft)
    ft.commit()
}

fun androidx.fragment.app.FragmentTransaction.useDefaultFragmentAnimations() {
    // https://stackoverflow.com/questions/4932462/animate-the-transition-between-fragments/33992609
    // https://stackoverflow.com/questions/4817900/android-fragments-and-animation
    this.setCustomAnimations(R.anim.fr_enter, R.anim.fr_leave, R.anim.pop_enter, R.anim.pop_leave)
}


fun AppCompatActivity.addFragment(fragment: androidx.fragment.app.Fragment, frameId: Int, tag: String? = null, addToBackStack: Boolean = false, func: ((ft: androidx.fragment.app.FragmentTransaction) -> Unit)? = null) {
    supportFragmentManager.inTransactionAdv { ft ->
        func?.invoke(ft)
        ft.add(frameId, fragment)
        if (!tag.isNullOrBlank() || addToBackStack)
            ft.addToBackStack(tag)
    }
}


fun AppCompatActivity.replaceFragment(fragment: androidx.fragment.app.Fragment, frameId: Int, tag: String? = null, addToBackStack: Boolean = false, func: ((ft: androidx.fragment.app.FragmentTransaction) -> Unit)? = null) {
    //    supportFragmentManager.inTransaction {
    //        replace(frameId, fragment)
    //    }
    supportFragmentManager.inTransactionAdv { ft ->
        func?.invoke(ft)
        ft.replace(frameId, fragment)
        if (!tag.isNullOrBlank() || addToBackStack)
            ft.addToBackStack(tag)
    }
}



