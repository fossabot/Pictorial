package com.messy.pictorial.mvvm

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

@SuppressLint("Registered")
abstract class Activity<VM : ViewModel> : AppCompatActivity() {

    @Suppress("MemberVisibilityCanBePrivate")
    lateinit var viewModel: VM

    abstract fun getViewModelClass(): Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(getViewModelClass())
    }
}