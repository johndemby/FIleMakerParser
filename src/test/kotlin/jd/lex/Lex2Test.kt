package jd.lex

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class Lex2Test
{
        @Test
        @DisplayName("Lexeme name Test")
        fun LexemeCheck1()
        {
            val l1 = LexName( "WIRTH", 1)
            assertEquals("WIRTH", l1.stringVal(), "Stringval of NAME")
            assertEquals(0, l1.intVal())
            assertEquals(1, l1.lineNum)
            assertEquals(1, l1.lineNo)
        }



        @Test
        @DisplayName("Simple Lexer Test")
        fun simpleLexer()
        {
            val dataStr : String = "this is a test"

            val subj : Lexer = Lexer(dataStr)

            var lex:Lexeme = subj.next()
            val expected =  LexName( "this", 1)
            assertEquals(lex, expected)

        }


        @Test
        @DisplayName("Sequence 1")
        fun lexerSeq1()
        {
            val dataStr =
                    """word 12
                 // ignore this
                ${'$'}stuff  22
                "one" two 'three' """
            val expected: List<Lexeme> = listOf(LexName("word", 1)
                    , LexNum(12, 1)
                    , LexKeyWord("\$stuff", 3)
                    , LexNum(22, 3)
                    , LexQString( "one", 4)
                    , LexName( "two", 4)
                    , LexQString("three",4)
                    , LexEOT(4))

            val subj: Lexer = Lexer(dataStr)

            expected.forEach { assertEquals(it, subj.next()) }
        }

}
