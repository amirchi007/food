package com.amir.dunifood.utils

interface BasePresenter<T> {
    fun onAttach(view: T)
    fun onDetach()
}

interface BaseView {
    // show functions for View
}