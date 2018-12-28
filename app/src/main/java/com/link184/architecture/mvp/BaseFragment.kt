package com.link184.architecture.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import com.link184.architecture.mvp.widgets.PowerView

/**
 * Top fragment component, all application fragments must extend that class
 */
abstract class BaseFragment<P : BasePresenter<*>> : androidx.fragment.app.DialogFragment(), BaseView {
    private val powerView: PowerView? by lazy {
        when (view) {
            is PowerView -> view as? PowerView
            is ViewGroup -> (view as ViewGroup).children.firstOrNull { it is PowerView } as? PowerView
            else -> null
        }
    }

    /**
     * Give me a instance of [BasePresenter] which will take care of all boring lifecycle related
     * logic.
     */
    protected abstract val presenter: P

    /**
     * Useful field for fragments
     */
    protected val baseActivity: BaseActivity<*>
        get() = activity as BaseActivity<*>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(onCreateView(), container, false)
    }

    /** Override it to set basic UI state */
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

    /** Override it to set custom general error handling */
    override fun onError(t: Throwable) {
        powerView?.showEmptyState()
        baseActivity.onError(t)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        powerView?.setOnRefreshListener(this)
        initViews()
        presenter.attachView()
    }

    fun setupToolbar(block: Toolbar.() -> Unit) {
        ((view as? ViewGroup)?.children?.firstOrNull {
            it is Toolbar
        } as? Toolbar)?.apply(block)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        presenter.killView()
        super.onDestroy()
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    protected fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    /**
     * @param component inject your class here.
     * @return layout resource id to be attached to this activity
     */
    @LayoutRes
    protected abstract fun onCreateView(): Int

    /**
     * Lets put activity result logic on presenter, almost all times that logic is not UI related.
     * Can by overridden to handle custom edge cases.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.handleActivityResult(resolveActivityState(requestCode, resultCode, data))
    }
}