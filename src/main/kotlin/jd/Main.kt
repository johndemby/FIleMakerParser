package jd

import java.io.InputStreamReader
import java.lang.System


fun main(args:Array<String>) {


    val inp : InputStreamReader = InputStreamReader(System.`in`)
    Lexer(inp).next()

}