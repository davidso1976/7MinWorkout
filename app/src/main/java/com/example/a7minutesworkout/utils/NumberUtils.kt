package com.example.a7minutesworkout.utils

fun Int.isInRange(a: Int) : Boolean {
    return this in (0)..<a
}