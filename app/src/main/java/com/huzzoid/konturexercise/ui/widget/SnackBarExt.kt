package com.huzzoid.konturexercise.ui.widget

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration).show()
}