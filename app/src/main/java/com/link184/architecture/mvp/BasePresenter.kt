package com.link184.architecture.mvp

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V : BaseView> {
    abstract val context: V
    /**
     * context bundle([BaseFragment.getArguments] or [BaseActivity.getIntent.getExtras])
     */
    protected val bundle by lazy {
        when (context) {
            is BaseFragment<*> -> (context as BaseFragment<*>).arguments
            is BaseActivity<*> -> (context as BaseActivity<*>).intent.extras
            else -> throw IllegalStateException("View is instance of incorrect class")
        }
    }

    /**
     * A stack of rxJava disposables. No need to touch, it is fully integrated with
     * android lifecycle. Can be delegated in custom edge cases.
     */
    val disposables = CompositeDisposable()

    /**
     * Override it to handle refresh UI action. The method is triggered from SwipeRefreshLayout.
     */
    open fun onRefresh() {
    }

    /**
     * Override it to all android lifecycle dependent logic. The method is called when an activity
     * is on onCreate or a fragment is on onViewCreated state.
     */
    open fun attachView() {
    }

    /**
     * Override it to all android lifecycle dependent logic. This method is called when activity or
     * fragment is onPause state.
     */
    open fun onPause() {
    }

    /**
     * Override it to all android lifecycle dependent logic. This method is called when activity or
     * fragment is onResume state.
     */
    open fun onResume() {
    }

    /**
     * Override it to all android lifecycle dependent logic. The method is called when an activity
     * is on onStop or a fragment is on onViewDestroyed state.
     */
    open fun detachView() {
        disposables.clear()
    }

    /**
     * Override it to all android lifecycle dependent logic. The method is called when an activity
     * or a fragment is on onDestroy state.
     */
    open fun killView() {
        disposables.dispose()
    }

    /**
     * Override it to handle onActivityResult from presenter in much developer friendly mode.
     */
    open fun handleActivityResult(activityResultState: ActivityResultState) {
    }
}