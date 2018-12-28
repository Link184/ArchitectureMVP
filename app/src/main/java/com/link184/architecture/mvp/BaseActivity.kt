package com.link184.architecture.mvp

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.link184.architecture.mvp.widgets.PowerView

/**
 * Top activity component, all application activities must extend that class
 */
abstract class BaseActivity<P : BasePresenter<*>> : AppCompatActivity(), BaseView {
    private val powerView: PowerView? by lazy {
        findViewById<ViewGroup>(android.R.id.content).children.firstOrNull {
            it is PowerView
        } as? PowerView
    }

    /**
     * Give me a instance of [BasePresenter] which will take care of all boring lifecycle related
     * logic.
     */
    protected abstract val presenter: P

    /** Override it to set basic UI state, is called in [AppCompatActivity.onCreate]*/
    override fun initViews() {
    }

    override fun onRefresh() {
        presenter.onRefresh()
    }

    override fun showProgress() {
        powerView?.showProgress()
    }

    override fun hideProgress() {
        powerView?.hideProgress()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreate()?.let { setContentView(it) }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        powerView?.setOnRefreshListener(this)
        presenter.attachView()
        initViews()
    }

    fun setToolBarTitle(value: String) {
        supportActionBar?.title = value
    }

    fun setToolBarTitle(value: Int) {
        supportActionBar?.setTitle(value)
    }

    protected fun hideKeyboard() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    /**
     * @return layout resource id to be attached to this activity
     */
    @LayoutRes
    protected abstract fun onCreate(): Int?

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
    }

    override fun onStop() {
        presenter.detachView()
        super.onStop()
    }

    override fun onDestroy() {
        presenter.killView()
        super.onDestroy()
    }

    /**
     * Handle all global errors. This method can be and is called from every context dependent
     * module.
     */
    override fun onError(t: Throwable) {
        powerView?.showEmptyState()
    }

    /**
     * Lets put activity result logic on presenter, almost all times that logic is not UI related.
     * Can by overridden to handle custom edge cases.
     */
    final override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.handleActivityResult(resolveActivityState(requestCode, resultCode, data))
    }
}