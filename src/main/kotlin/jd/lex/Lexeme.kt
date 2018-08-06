package jd.lex


sealed class Lexeme(val lineNo : Int)
{
    abstract fun stringVal() : String
    abstract fun intVal() : Int
}

data class LexName (val lexVal : String , val lineNum : Int) : Lexeme(lineNum)
{
    override fun stringVal(): String = lexVal
    override fun intVal(): Int = 0
}

data class LexNum(val lexVal : Int,  val lineNum : Int) : Lexeme(lineNum)
{
    override fun stringVal()  = lexVal.toString()
    override fun intVal()  = lexVal
}


data class LexKeyWord(val lexVal : String, val lineNum : Int) : Lexeme(lineNum)
{
    override fun stringVal()  = lexVal
    override fun intVal()  = 0
}


data class LexQString(val lexVal : String, val lineNum : Int) : Lexeme(lineNum)
{
    override fun stringVal()  = lexVal
    override fun intVal()  = 0
}


data class LexEOT( val lineNum : Int) : Lexeme(lineNum)
{
    override fun stringVal()  = ""
    override fun intVal()  = 0
}

data class LexChar(val lexVal : Int, val lineNum : Int) : Lexeme(lineNum)
{
    private val ch  = (lexVal as Int).toChar()

    override fun stringVal() = "$ch"
    override fun intVal()  = lexVal
}

data class LexErr(val lexVal : Int, val lineNum : Int) : Lexeme(lineNum)
{
    override fun stringVal() = "**ERROR**"
    override fun intVal() = lexVal
}


