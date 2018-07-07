package jd

import java.io.Reader
import java.io.StreamTokenizer
import java.io.StringReader


enum class LexType { LexName, LexNum, LexKeyWord, LexQString, LexEOT, LexChar, LexErr }

class Lexeme (val lexType : LexType, val lexVal : Any)
{
    val stringVal : String
    val intVal : Int

    init
    {
        when (lexType)
        {
            LexType.LexName, LexType.LexKeyWord ->
            {
                stringVal = lexVal as String
                intVal = 0
            }
            LexType.LexNum ->
            {
                stringVal = ""
                intVal = lexVal as Int
            }
            LexType.LexEOT ->
            {
                stringVal = ""
                intVal = 0
            }

            LexType.LexQString ->
            {
                stringVal = lexVal as String
                intVal = 0
            }

            LexType.LexChar ->
            {
                val ch : Char  = (lexVal as Int).toChar()
//                println("LexChar - lexVal : $lexVal, ch => $ch")
                stringVal = "$ch"
                intVal = 0
            }

            LexType.LexErr ->
            {
                stringVal = "**ERROR**"
                intVal = lexVal as Int
            }

        }
    }

    override fun toString(): String
    {
        return "Lexeme(lexType=$lexType, lexVal=$lexVal, stringVal='$stringVal', intVal=$intVal)"
    }

    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Lexeme

        if (lexType != other.lexType) return false
        if (lexVal != other.lexVal) return false
        if (stringVal != other.stringVal) return false
        if (intVal != other.intVal) return false

        return true
    }

    override fun hashCode(): Int
    {
        var result = lexType.hashCode()
        result = 31 * result + lexVal.hashCode()
        result = 31 * result + stringVal.hashCode()
        result = 31 * result + intVal
        return result
    }
}


class Lexer
{
    constructor (rdr : Reader)
    {
        toker = makeLexer(rdr)
    }

    constructor (str: String)
    {
        toker = makeLexer(StringReader(str))
    }

    fun next() : Lexeme
    {
        val retValue : Lexeme

        toker.nextToken()
        when (toker.ttype)
        {
            StreamTokenizer.TT_EOF -> retValue = Lexeme(LexType.LexEOT, "")

            StreamTokenizer.TT_NUMBER ->  retValue = Lexeme(LexType.LexNum, toker.nval.toInt())

            StreamTokenizer.TT_WORD ->
                retValue =                 if (toker.sval.startsWith("$"))
                        Lexeme(  LexType.LexKeyWord, toker.sval)
                    else
                        Lexeme( LexType.LexName, toker.sval)

            tokSingleQuote, tokDoubleQuote ->  retValue = Lexeme(LexType.LexQString, toker.sval)

            tokOpenParen, tokCloseParen -> retValue = Lexeme(LexType.LexChar, toker.ttype)

            else ->
            {
//                println("unexpected token type " + toker.ttype.toString() + toker.sval)
                retValue = Lexeme(LexType.LexErr, toker.nval.toInt())
            }

        }
        return retValue
    }


    fun makeLexer(rdr : Reader) : StreamTokenizer
    {
        val dollar : Char = '\$'
        val retval = StreamTokenizer(rdr)
        retval.wordChars(dollar.toInt(), dollar.toInt())
        retval.slashSlashComments(true)
        retval.slashStarComments(true)
        retval.quoteChar(tokDoubleQuote)
        retval.quoteChar(tokSingleQuote)

        return retval
    }

    private val toker : StreamTokenizer

    val tokDoubleQuote : Int = 34
    val tokSingleQuote : Int = 39
    val tokOpenParen   : Int = 40
    val tokCloseParen  : Int = 41
}