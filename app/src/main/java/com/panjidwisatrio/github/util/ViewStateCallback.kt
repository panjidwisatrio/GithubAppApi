package com.panjidwisatrio.github.util

import android.view.View

// untuk menampilkan state dari view dan mengembalikan nilai visibility
interface ViewStateCallback<T> {
    fun onSuccess(data: T)
    fun onLoading()
    fun onFailed(message: String?)

    val invisible: Int
        get() = View.INVISIBLE

    val visible: Int
        get() = View.VISIBLE
}