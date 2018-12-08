package com.messy.util

fun <T> lazyNone(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

fun <T> lazyPublic(initializer: () -> T) = lazy(LazyThreadSafetyMode.PUBLICATION, initializer)
