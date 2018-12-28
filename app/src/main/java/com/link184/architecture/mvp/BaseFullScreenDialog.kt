package com.link184.architecture.mvp

import android.os.Bundle

abstract class BaseFullScreenDialog<P : BasePresenter<*>> : BaseFragment<P>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(androidx.fragment.app.DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
    }
}