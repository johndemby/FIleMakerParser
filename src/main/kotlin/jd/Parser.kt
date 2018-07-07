package jd

import java.io.Reader
import java.util.*


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

    fun parseIt(input : Reader) : List<OpCode>
    {
        val retval : MutableList<OpCode> = LinkedList()
        val lexer : Lexer = Lexer(input)

        var lexeme : Lexeme = lexer.next()


        while (lexeme.lexType != LexType.LexEOT)
        {
            when (lexeme.lexType)
            {
                LexType.LexName ->
                {
                    retval.add(
                            when (lexeme.stringVal.toUpperCase())
                            {
                                "DEFINE" -> processDefine(lexer)
                                "TYPE" -> processTypeRef(lexer)

                                else -> throw BadDataException("*** ERROR expected \"DEFINE\" but got ${lexeme.stringVal}")
                            }
                    )
                }

                else -> throw BadDataException("Invalid token, expected keyword \"DEFINE\" but got $lexeme")
            }
            lexeme = lexer.next()
        }
        return retval
    }

    fun processDefine(lexer : Lexer) : OpCode
    {
        val lex = lexer.next()

        return when (lex.stringVal)
        {
            "TYPE" -> processTypeDef(lexer)
            else -> throw BadDataException(" DEFINE - invalid word, expecting \"TYPE\" got $lex")
        }

    }


    private fun processTypeDef(lexer : Lexer) : OpCode
    {
        var l1 : Lexeme = lexer.next()

        if (l1.lexType != LexType.LexName)
        {
            throw BadDataException(" TYPE - invalid name, expecting a ${LexType.LexName} got $l1")
        }

        val typeName = l1.stringVal
        val fieldList : List<String>

        l1 = lexer.next()
        if (l1.stringVal == "(")
        {
            fieldList = processFieldList(lexer)
            return OpTypeDef(typeName, fieldList)
            //println ("TYPEDEF \"$typeName\"  fields $fieldList")
        }
        else
        {
            throw BadDataException("TypeDef expected Char '(', got $l1")
        }

    }

    private fun processFieldList(lexer : Lexer) : List<String>
    {

        var token = lexer.next()
        val retVal : MutableList<String> = LinkedList()
        while (token.stringVal != ")" )
        {

            if (token.lexType == LexType.LexName)
            {
//                System.err.println("FLDLIST: ${token.stringVal}")
                retVal.add(token.stringVal)
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
        if  (token.lexType != LexType.LexName)
            throw BadDataException("Expecting a NAME , but got ${token}")
        else
            return OpTypeRef(token.stringVal)

    }


}