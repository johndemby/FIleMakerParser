package jd.lex

import java.io.Reader
import java.io.StreamTokenizer
import java.io.StringReader

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
            StreamTokenizer.TT_EOF -> retValue = LexEOT(toker.lineno())

            StreamTokenizer.TT_NUMBER ->  retValue = LexNum(toker.nval.toInt(), toker.lineno())

            StreamTokenizer.TT_WORD ->
                retValue =  if (toker.sval.startsWith("$"))
                    LexKeyWord( toker.sval, toker.lineno())
                else
                    LexName( toker.sval, toker.lineno())

            tokSingleQuote, tokDoubleQuote ->  retValue = LexQString( toker.sval, toker.lineno())

            tokOpenParen, tokCloseParen -> retValue = LexChar(toker.ttype, toker.lineno())

            else ->
            {
                println("unexpected token type " + toker.ttype.toString() + toker)
                retValue = LexErr(toker.nval.toInt(), toker.lineno())
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