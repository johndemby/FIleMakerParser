package jd

import jd.lex.LexEOT
import jd.lex.LexName
import java.io.LineNumberReader
import java.io.Reader
import java.util.*
import jd.lex.Lexer
import jd.lex.Lexeme


class BadDataException (msg : String) : Exception(msg)

/**
 * The grammar is
 *
 *   TYPE <name> '(' <nameList> ')'
 *   nameList := name | name "," nameList
 *
 *


type blah

header

write

record (col1 7, col2 "B", col3 "M")

write

record (col1 8, col2 A, col3 "M")

write

save foo

(col4 "x")

write

copy foo  (col3 "M2")

write

 *
 *
 *
 */

class Parser
{


    fun parseIt(rdr : Reader) : List<OpCode>
    {

        val retval : MutableList<OpCode> = LinkedList()
        val lexer : Lexer = Lexer(rdr)
        var lexeme : Lexeme = lexer.next()
        val initKeywords = "DEFINE, TYPE, WRITE, HEADER, COPY"

        while (lexeme !is LexEOT)
        {
            when (lexeme)
            {
                is LexName ->
                {
                    retval.addAll(
                            when (lexeme.stringVal().toUpperCase())
                            {
                                "DEFINE" -> listOf(processDefine(lexer))
                                "TYPE"   -> listOf(processTypeRef(lexer))
                                "WRITE"  -> listOf(OpWrite())
                                "HEADER" -> listOf(OpHeader())
                                "COPY"   -> listOf(processCopy(lexer))
                                //"RECORD" -> OpRecord()

                                else -> error(lexeme.lineNum, "*** ERROR expected one of $initKeywords but got ${lexeme.stringVal()}")
                            }
                    )
                }
                else -> error(lexeme.lineNo, "Invalid token, expected  a NAME  got $lexeme")
            }
            lexeme = lexer.next()
        }
        return retval
    }


    private fun error(lineNum : Int, msg : String) : Nothing
    {
        throw BadDataException("***Error on line $lineNum : $msg")
    }


    fun processCopy(lexer : Lexer) : OpCode
    {
        val lex = lexer.next()

        if (lex  !is jd.lex.LexName)
            BadDataException(" COPY - expected name got $lex ")

        return OpCopy(lex.stringVal())
    }


    fun processDefine(lexer : Lexer) : OpCode
    {
        val lex = lexer.next()

        return when (lex.stringVal().toUpperCase())
        {
            "TYPE" -> processTypeDef(lexer)
            else -> error(lex.lineNo, " DEFINE - invalid word, expecting \"TYPE\" got $lex")
        }

    }


    private fun processTypeDef(lexer : Lexer) : OpCode
    {
        var l1 : Lexeme = lexer.next()

        if (l1 !is LexName)
        {
            error(l1.lineNo, "TYPE - invalid name, expecting a NAME got $l1")
        }

        val typeName = l1.stringVal()
        val fieldList : List<String>

        l1 = lexer.next()
        if (l1.stringVal() == "(")
        {
            fieldList = processFieldList(lexer)
            return OpTypeDef(typeName, fieldList)
            //println ("TYPEDEF \"$typeName\"  fields $fieldList")
        }
        else
        {
            error(l1.lineNo, "TypeDef expected Char '(', got $l1")
        }

    }

    private fun processFieldList(lexer : Lexer) : List<String>
    {

        var token = lexer.next()
        val retVal : MutableList<String> = LinkedList()
        while (token.stringVal() != ")" )
        {

            if (token is LexName)
            {
//                System.err.println("FLDLIST: ${token.stringVal}")
                retVal.add(token.stringVal())
            }
            else
            {
                throw BadDataException(" FIELDLIST - expected name or ')' , got $token")
            }

            token = lexer.next()
        }

        return retVal

    }


    fun processTypeRef(lexer : Lexer) : OpCode
    {
        val token = lexer.next()
        if  (token !is LexName)
            error(token.lineNo, "Expecting a NAME , but got ${token}")
        else
            return OpTypeRef(token.stringVal())

    }


}

