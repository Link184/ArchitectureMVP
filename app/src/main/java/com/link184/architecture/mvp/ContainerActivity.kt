package com.link184.architecture.mvp

import android.os.Bundle

/**
 * Designed to be extended by empty activities outside of MVP pattern(no activity presenter, no
 * activity view). A container activity hold a empty MVP presenter. Useful for cases when a UI layout
 * must been rendered from activity or fragment.
 * @param FP fragment presenter
 * @param F fragment
 */
abstract class ContainerActivity<FP : BasePresenter<*>, F : BaseFragment<FP>> : BaseActivity<BasePresenter<*>>() {
    /** IGNORE IT*/
    final override var presenter: BasePresenter<*> = EmptyPresenter

    /** IGNORE IT*/
    final override fun onCreate(): Int? = R.layout.activity_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, onCreateActivity())
                .commit()
    }

    /** Give me a instance of desired fragment*/
    abstract fun onCreateActivity(): F

    private object EmptyPresenter : BasePresenter<BaseView>() {
        override val context: BaseView
            get() = object : BaseView {

                /** IGNORE IT*/
                override fun initViews() {
                }

                /** IGNORE IT*/
                override fun onError(t: Throwable) {
                }

                /** IGNORE IT*/
                override fun showProgress() {
                }

                /** IGNORE IT*/
                override fun hideProgress() {
                }

                /** IGNORE IT*/
                override fun onRefresh() {
                }
            }
    }
}