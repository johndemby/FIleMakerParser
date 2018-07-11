package jd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import java.io.StringReader
import kotlin.test.Test
import kotlin.test.assertFailsWith


class ParserTest
{

    @Test
    @DisplayName("Type definition test")
    fun testTypeDef()
    {
        val dataStr = " define TYPE mytype ( fldA fldB )"

        val  parser = Parser()

        val actual : List<OpCode> = parser.parseIt(StringReader(dataStr))
        val expected = listOf(OpTypeDef("mytype", listOf("fldA", "fldB")))

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("TypeDef missing name")
    fun testBadTypeDef()
    {
        // missing typename
        val dataStr = " define TYPE ( fldA fldB )"

        val  parser = Parser()

        assertFailsWith(BadDataException::class) {parser.parseIt(StringReader(dataStr))}

    }

    @Test
    @DisplayName("TypeDef missing parens")
    fun testBadTypeDef_2()
    {
        // missing parens on field list
        val dataStr = " define TYPE thetype fldA fldB "
        val  parser = Parser()
        assertFailsWith(BadDataException::class) {parser.parseIt(StringReader(dataStr))}

    }

    @Test
    @DisplayName("TypeDef no field list")
    fun testBadTypeDef_3()
    {
        // missing typename
        val dataStr = " define TYPE AType"

        val  parser = Parser()

        assertFailsWith(BadDataException::class) {parser.parseIt(StringReader(dataStr))}

    }

    @Test
    @DisplayName("TypeDef - good")
    fun testGoodTypeDef()
    {
        val dataStr = "define TYPE goodType (col1 col2 col3)"
        val expected : List<OpCode> = listOf(OpTypeDef("goodType", listOf("col1", "col2", "col3")))
        val parser = Parser()
        val opcodes = parser.parseIt(StringReader(dataStr))
        assertEquals(expected, opcodes)
    }



    @Test
    fun testWrite()
    {
        val dataStr = "write  "
        val expected : List<OpCode> = listOf(OpWrite())
        val parser = Parser()
        val opcodes = parser.parseIt(StringReader(dataStr))
        assertEquals(expected, opcodes)
    }


    @Test
    fun testHeader()
    {
        val dataStr = "heaDer  "
        val expected : List<OpCode> = listOf(OpHeader())
        val parser = Parser()
        val opcodes = parser.parseIt(StringReader(dataStr))
        assertEquals(expected, opcodes)
    }


    @Test
    @DisplayName("valid type reference")
    fun testTypeRef()
    {
        val dataStr = "type sometype "
        val expected: List<OpCode> = listOf(OpTypeRef("sometype"))
        val parser = Parser()
        val opcodes = parser.parseIt(StringReader(dataStr))
        assertEquals(expected, opcodes)
    }

    @Test
    @DisplayName("bad type reference no name")
    fun testTypeRefNoName()
    {
        val dataStr = "type  define type q( a b c) "
        val parser = Parser()
     //   assertFailsWith (BadDataException::class)
       parser.parseIt(StringReader(dataStr))
    }
}