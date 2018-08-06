package jd

import jd.lex.*
import java.io.Reader
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory


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

record (col1 7 col2 "B" col3 "M")

write

record (col1 8 col2 A col3 "M")


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

    private val logger : Logger = LoggerFactory.getLogger("jd.Parser")

    fun parseIt(rdr : Reader) : List<OpCode>
    {

        val retval : MutableList<OpCode> = LinkedList()
        val lexer  = Lexer(rdr)
        var lexeme : Lexeme = lexer.next()
        val initKeywords = "DEFINE, TYPE, WRITE, HEADER, COPY"

        logger.debug("first lexeme is $lexeme")

        while (lexeme !is LexEOT)
        {
            logger.debug ("found lexeme $lexeme")
            when (lexeme)
            {
                is LexName ->
                {
                    retval.addAll(
                            when (lexeme.stringVal().toUpperCase())
                            {
                                "DEFINE" -> listOf(processDefine(lexer))
                                "TYPE"   -> listOf(processTypeRef(lexer))
                                "WRITE"  -> listOf(OpWrite(lexeme.lineNum))
                                "HEADER" -> listOf(OpHeader(lexeme.lineNum))
                                "COPY"   -> listOf(processCopy(lexer))
                                "RECORD" -> processRecord(lexer, lexeme.lineNum)

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


    private fun processCopy(lexer : Lexer) : OpCode
    {
        val lex = lexer.next()

        if (lex  !is jd.lex.LexName)
            BadDataException(" COPY - expected name got $lex ")

        return OpCopy( lex.lineNo,  lex.stringVal())
    }


    private fun processDefine(lexer : Lexer) : OpCode
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
        val l1 : Lexeme = lexer.next()

        if (l1 !is LexName)
        {
            error(l1.lineNo, "TYPE - invalid name, expecting a NAME got $l1")
        }

        val typeName = l1.stringVal()
        val fieldList : List<String>
        val lineNo = l1.lineNum

        val l2 = lexer.next()
        if (l2.stringVal() == "(")
        {
            fieldList = processFieldList(lexer)
            return OpTypeDef(lineNo, typeName, fieldList)
            //println ("TYPEDEF \"$typeName\"  fields $fieldList")
        }
        else
        {
            error(l2.lineNo, "TypeDef expected Char '(', got $l2")
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


   private fun processTypeRef(lexer : Lexer) : OpCode
    {
        val token = lexer.next()
        if  (token !is LexName)
            error(token.lineNo, "Expecting a NAME , but got $token")
        else
            return OpTypeRef(token.lineNum, token.stringVal())

    }


    private fun processRecord(lexer : Lexer, lineNum : Int) : List<OpCode>
    {
        val token = lexer.next()
        return if (token.stringVal() == "(")
            listOf(OpRecord(lineNum)) + processNameValuePairs(lexer, listOf())
        else
            error(token.lineNo, "Expecting \"(\", but got $token")
    }

    private tailrec fun processNameValuePairs(lexer: Lexer, prevCodes : List<OpCode>): List<OpCode>
    {
        val token1 = lexer.next()

        if (token1 is LexChar && token1.stringVal() == ")") return prevCodes
        if (token1 !is LexName) error(token1.lineNo, "Fieldlist, expected a NAME or \")\", but got $token1")

        val fldName = token1.stringVal()
        val fldValueToken = lexer.next()

        return when (fldValueToken)
        {
            is LexQString,
            is LexNum ->
            {
                val fldValue = fldValueToken.stringVal()
                val codeList = prevCodes + OpSet(token1.lineNo, fldName, fldValue)
                processNameValuePairs(lexer, codeList)
            }
            else ->
                error(fldValueToken.lineNo, "Expected a Number or Quoted String but got $fldValueToken")
        }

    }  // processNameValuePairs


}

