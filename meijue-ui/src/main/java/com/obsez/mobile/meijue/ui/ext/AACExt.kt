package com.obsez.mobile.meijue.ui.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders


// Extensions for Architecture Components ViewModels

inline fun <reified T : ViewModel> androidx.fragment.app.Fragment.getViewModel(factory: ViewModelProvider.Factory) = ViewModelProviders.of(this, factory).get(T::class.java)

inline fun <reified T : ViewModel> androidx.fragment.app.FragmentActivity.getViewModel(factory: ViewModelProvider.Factory) = ViewModelProviders.of(this, factory).get(T::class.java)

