package com.dapascript.pokecat.utils

fun capitalizeFirstLetter(string: String): String {
    return string.substring(0, 1).uppercase() + string.substring(1)
}