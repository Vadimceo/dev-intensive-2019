package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard(){
    val inputManager:InputMethodManager =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)
}

fun Activity.isKeyboardOpen(): Boolean {
    val visibleBounds = Rect()
    val rootView: View = window.decorView.findViewById(android.R.id.content)
    rootView.getWindowVisibleDisplayFrame(visibleBounds)

    val heightDiff = rootView.rootView.height - (visibleBounds.bottom - visibleBounds.top);
    return heightDiff > rootView.rootView.height / 4

}

fun Activity.isKeyboardClosed(): Boolean {
    return !this.isKeyboardOpen()
}