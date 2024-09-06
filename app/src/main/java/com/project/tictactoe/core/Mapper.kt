package com.project.tictactoe.core

fun <IN : Any, OUT : Any> IN.mapFrom(block: (IN) -> OUT): OUT = block(this)
