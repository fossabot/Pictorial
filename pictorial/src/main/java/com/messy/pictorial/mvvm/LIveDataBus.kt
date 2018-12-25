/*
package com.messy.pictorial.mvvm

import androidx.collection.SparseArrayCompat
import androidx.lifecycle.*

class LiveDataBus private constructor() : LiveData<Any>() {

    companion object {
        private val lock = Any()
        private val map = SparseArrayCompat<LiveDataBus>()

        fun get(name: String): LiveDataBus {
            val key = name.hashCode()
            return map.get(key) ?: run {
                synchronized(lock) {
                    map.get(key) ?: run {
                        map.put(key, LiveDataBus())
                        map.get(key)
                    }
                }
            }!!
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in Any>) {
        super.observe(owner, observer)
    }

    class StickyControlObserver<T>(
        private val observer: Observer<T>,
        private val owner: LifecycleOwner
    ) : Observer<T> {
        private val lifecycleObserver = LifecycleObserver()


        init {
            owner.lifecycle.addObserver(lifecycleObserver)
        }

        override
        fun onChanged(t: T) {
            if (lifecycleObserver.lastState == Lifecycle.State.STARTED &&
                lifecycleObserver.curState == Lifecycle.State.RESUMED
            )
                observer.onChanged(t)
        }

    }

    class LifecycleObserver : GenericLifecycleObserver {
        companion object {
            private val NO_SET = Lifecycle.State.INITIALIZED
        }

        var lastState = NO_SET
        var curState = NO_SET

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            lastState = curState
            curState = source.lifecycle.currentState
            if (event == Lifecycle.Event.ON_DESTROY) {
                source.lifecycle.removeObserver(this)
            }
        }

    }
}
*/
