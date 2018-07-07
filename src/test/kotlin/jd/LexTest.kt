package jd

import kotlin.test.assertEquals
import kotlin.test.fail
import kotlin.test.Test

import org.junit.jupiter.api.DisplayName
import java.rmi.dgc.Lease


class LexTest
{

    @Test
    @DisplayName("Lexeme name Test")
    fun LexemeCheck1()
    {
        val l1 = Lexeme(LexType.LexName, "WIRTH")
        assertEquals("WIRTH", l1.stringVal, "Stringval of NAME")
        assertEquals(0, l1.intVal)
    }

    @Test
    @DisplayName("Lexeme num Test")
    fun LexemeNumCheck()
    {
        val l1 = Lexeme(LexType.LexNum, 42)
        assertEquals("", l1.stringVal, "Stringval of NAME")
        assertEquals(42, l1.intVal)
    }


    @Test
    @DisplayName("Lexeme key Test")
    fun LexemeKeyCheck()
    {
        val l1 = Lexeme(LexType.LexKeyWord, "HAPPY")
        assertEquals("HAPPY", l1.stringVal)
        assertEquals(0, l1.intVal)
    }

    @Test
    @DisplayName("Lexeme Single Quote Test")
    fun LexemeSQuoteCheck()
    {
        val l1 = Lexeme(LexType.LexQString, "HAPPY")
        assertEquals("HAPPY", l1.stringVal)
        assertEquals(0, l1.intVal)
    }


    @Test
    @DisplayName("Lexeme Double Quote Test")
    fun LexemeDblQuoteCheck()
    {
        val str = "Walk"
        val l1 = Lexeme(LexType.LexQString, str)
        assertEquals(str, l1.stringVal)
        assertEquals(0, l1.intVal)
    }


    @Test
    @DisplayName("Lexeme err Test")
    fun LexemeErrCheck()
    {
        val l1 = Lexeme(LexType.LexErr, 5)
        assertEquals("**ERROR**", l1.stringVal)
        assertEquals(5, l1.intVal)
    }




    @Test
    @DisplayName("Simple Lexer Test")
    fun simpleLexer()
    {
        val dataStr : String = "this is a test"

        val subj : Lexer = Lexer(dataStr)

        var lex:Lexeme = subj.next()
        val expected =  Lexeme(LexType.LexName, "this")

        assertEquals(lex, expected)

    }


    @Test
    @DisplayName("Sequence 1")
    fun lexerSeq1()
    {
        val dataStr = "word 12\n  // ignore this \n   \$stuff  22\n \"one\" two 'three' "
        val expected: List<Lexeme> = listOf(Lexeme(LexType.LexName, "word")
                , Lexeme(LexType.LexNum, 12)
                , Lexeme(LexType.LexKeyWord, "\$stuff")
                , Lexeme(LexType.LexNum, 22)
                , Lexeme(LexType.LexQString, "one")
                , Lexeme(LexType.LexName, "two")
                , Lexeme(LexType.LexQString, "three")
                , Lexeme(LexType.LexEOT, ""))

        val subj: Lexer = Lexer(dataStr)

        expected.forEach { assertEquals(it, subj.next()) }
    }


}