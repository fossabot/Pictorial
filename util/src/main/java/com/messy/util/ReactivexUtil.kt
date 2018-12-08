@file:Suppress("unused")

package com.messy.util

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

inline fun Disposable?.disposeChecked() {
    if (this != null && !this.isDisposed) this.dispose()
}

fun <T> Observable<T>.start(block: (it: T) -> Unit): SubscribeBuilder<T> {
    val builder = SubscribeBuilder(this)
    builder.next(block)
    return builder
}

fun <T> Observable<T>.next(block: (it: T) -> Unit) {
    val builder = SubscribeBuilder(this)
    builder.next(block)
    builder.done()
}

fun <T> Observable<T>.once(block: (it: T) -> Unit) {
    val builder = SubscribeBuilder(this)
    val nextLambda = { it: T ->
        block.invoke(it)
        builder.disposable.disposeChecked()
    }
    builder.subscribe { builder.disposable = it }
    builder.complete { builder.disposable.disposeChecked() }
    builder.next(nextLambda)
    builder.done()
}

@Suppress("unused")
class SubscribeBuilder<T>(private val observable: Observable<T>) {

    internal var disposable: Disposable? = null
    private var subscribeLambda: (disposable: Disposable) -> Unit = {}
    private var completeLambda: () -> Unit = {}
    private var nextLambda: (it: T) -> Unit = {}
    private var errorLambda: (t: Throwable) -> Unit = {}

    @SuppressLint("CheckResult")
    fun done() {
        observable.subscribe(nextLambda, errorLambda, completeLambda, subscribeLambda)
    }

    fun subscribe(block: (disposable: Disposable) -> Unit): SubscribeBuilder<T> {
        subscribeLambda = block
        return this
    }

    fun complete(block: () -> Unit): SubscribeBuilder<T> {
        completeLambda = block
        return this
    }

    fun error(block: (t: Throwable) -> Unit): SubscribeBuilder<T> {
        errorLambda = block
        return this
    }

    fun next(block: (it: T) -> Unit): SubscribeBuilder<T> {
        nextLambda = block
        return this
    }
}