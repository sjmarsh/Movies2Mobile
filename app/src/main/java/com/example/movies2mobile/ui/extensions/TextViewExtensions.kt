package com.example.movies2mobile.ui.extensions

import android.view.MotionEvent
import android.widget.TextView

// Ref: https://medium.com/@dimabatyuk/adding-clear-button-to-edittext-9655e9dbb721
public fun TextView.setOnRightDrawableClicked(onClicked: (view: TextView) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if(v is TextView) {
            if (event.x >= v.width - v.totalPaddingRight) {
                if(event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}
