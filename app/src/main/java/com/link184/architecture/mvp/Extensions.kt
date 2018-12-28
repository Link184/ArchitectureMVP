package com.link184.architecture.mvp

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

/**
 * Custom subscription which will bind each disposable to android lifecycle thought [BasePresenter]
 * @param presenter a instance of [BasePresenter]
 * @param onNext what should I do when is onNext
 * @param onError what should I do when is onError, by default I will pass the error to [BaseView]
 * @param onComplete what should I do when is onComplete, by default do nothing
 * @param onSubscribe what should I do when is onComplete, by default do nothing
 */
fun <T> Observable<T>.subscribe(
    presenter: BasePresenter<*>,
    onNext: (T) -> Unit,
    onError: (Throwable) -> Unit = presenter.context::onError,
    onComplete: () -> Unit = { presenter.context.hideProgress() },
    onSubscribe: (Disposable) -> Unit = { presenter.context.showProgress() }
) {
    observeOn(AndroidSchedulers.mainThread())
        .subscribe(onNext, onError, onComplete, onSubscribe)
        .also { presenter.disposables += it }
}