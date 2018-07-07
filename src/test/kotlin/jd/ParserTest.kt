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


}