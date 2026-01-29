package com.dnavarro.espectro.utils

fun <T> MutableList<T>.onBack() {
    if (size > 1) removeLastOrNull()
}