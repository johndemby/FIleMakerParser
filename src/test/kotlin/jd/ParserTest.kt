package jd

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Assertions.assertThrows
import java.io.StringReader

class ParserTest
{
    @Test
    @DisplayName("Type (UC) definition test")
    fun testTypeDefUC()
    {
        val dataStr = " define TYPE mytype ( fldA fldB )"

        val  parser = Parser()

        val actual : List<OpCode> = parser.parseIt(StringReader(dataStr))
        val expected = listOf(OpTypeDef(1,"mytype", listOf("fldA", "fldB")))

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("Type definition lowercase")
    fun testTypeDefLC()
    {
        val dataStr = " define type mytype ( fldA fldB )"

        val  parser = Parser()

        val actual : List<OpCode> = parser.parseIt(StringReader(dataStr))
        val expected = listOf(OpTypeDef(1, "mytype", listOf("fldA", "fldB")))

        assertEquals(expected, actual)
    }



    @Test
    @DisplayName("TypeDef missing name")
    fun testBadTypeDef()
    {
        // missing typename
        val dataStr = " define TYPE ( fldA fldB )"

        val  parser = Parser()

        assertThrows(BadDataException::class.java) {parser.parseIt(StringReader(dataStr))}

    }

    @Test
    @DisplayName("TypeDef missing parens")
    fun testBadTypeDef_2()
    {
        // missing parens on field list
        val dataStr = " define TYPE thetype fldA fldB "
        val  parser = Parser()
        assertThrows(BadDataException::class.java) {parser.parseIt(StringReader(dataStr))}

    }

    @Test
    @DisplayName("TypeDef no field list")
    fun testBadTypeDef_3()
    {
        // missing typename
        val dataStr = " define TYPE AType"

        val  parser = Parser()

        assertThrows(BadDataException::class.java) {parser.parseIt(StringReader(dataStr))}

    }

    @Test
    @DisplayName("TypeDef - good")
    fun testGoodTypeDef()
    {
        val dataStr = "define TYPE goodType (col1 col2 col3)"
        val expected : List<OpCode> = listOf(OpTypeDef(1, "goodType", listOf("col1", "col2", "col3")))
        val parser = Parser()
        val opcodes = parser.parseIt(StringReader(dataStr))
        assertEquals(expected, opcodes)
    }

    @Test
    fun testWrite()
    {
        val dataStr = "write  "
        val expected : List<OpCode> = listOf(OpWrite(1))
        val parser = Parser()
        val opcodes = parser.parseIt(StringReader(dataStr))
        assertEquals(expected, opcodes)
    }

    @Test
    fun testHeader()
    {
        val dataStr = "heaDer  "
        val expected : List<OpCode> = listOf(OpHeader(1))
        val parser = Parser()
        val opcodes = parser.parseIt(StringReader(dataStr))
        assertEquals(expected, opcodes)
    }

    @Test
    @DisplayName("valid type reference")
    fun testTypeRef()
    {
        val dataStr = "type sometype "
        val expected: List<OpCode> = listOf(OpTypeRef(1,"sometype"))
        val parser = Parser()
        val opcodes = parser.parseIt(StringReader(dataStr))
        assertEquals(expected, opcodes)
    }

    @Test
    @DisplayName("bad type reference no name")
    fun testTypeRefNoName()
    {
        val dataStr = "type ( a b c) "
        val parser = Parser()
        assertThrows(BadDataException::class.java) {
            parser.parseIt(StringReader(dataStr))
        }
    }




    @Test
    @DisplayName("Just for fun")
    fun justForFun()
    {
        val dataStr = """
            define TYPE mytype (col1 col2 col3)
            define TYPE mytype2 (col4 col5 col6)
            type mytype
            write
            """
        println("fun says :")
        Parser().parseIt(StringReader(dataStr)).forEach {println("  $it")}
        assert(true)

    }


    @Test
    @DisplayName("Empty Record test")
    fun recTest1()
    {
        val dataStr = "RECORD ()"

        val parser = Parser()
        val opCodes = parser.parseIt(StringReader(dataStr))
        assertEquals(1, opCodes.size, "Should only be one op code")
        assertEquals(listOf(OpRecord(1)), opCodes)
    }


    @Test
    @DisplayName("Record test")
    fun recTest2()
    {
        val dataStr = """RECORD (col1 'A' col2 12 col3 "123") """

        val parser = Parser()
        val opCodes = parser.parseIt(StringReader(dataStr))
        val expected  : List<OpCode> = listOf(OpRecord(1)
                , OpSet(1, "col1", "A")
                , OpSet(1, "col2", "12")
                , OpSet(1, "col3", "123"))

        assertEquals(expected, opCodes)


    }

}