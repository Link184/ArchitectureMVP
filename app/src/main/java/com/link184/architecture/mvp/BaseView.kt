package com.link184.architecture.mvp

/**
 * Top view component, all activities and fragments must implement it.
 */
interface BaseView : androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
    fun initViews()
    fun showProgress()
    fun hideProgress()
    fun onError(t: Throwable)
}