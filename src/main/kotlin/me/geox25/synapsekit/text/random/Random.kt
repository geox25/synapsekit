package me.geox25.synapsekit.text.random

fun generateRandomString(length: Int = 12): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun generateRandomNumberString(length: Int = 12): String {
    val allowedChars = ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

