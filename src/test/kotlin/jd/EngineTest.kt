package jd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.Ignore
import java.io.StringWriter
import kotlin.test.assertFailsWith


class EngineTest {


    @Test
//@DisplayName("Header without type ref ")
    @Ignore
    fun newTestGood()
    {
        val colList = listOf("col1", "colb", "colIII")
        val verbList: List<OpCode> = listOf(OpTypeDef("T1", colList),
                OpTypeRef("T1"),
                OpHeader())

        val res : StringList =  Engine.generate(verbList)
        assertEquals(1, res.size, "should be header only")
        assertEquals("col1|colb|colIII", res[0])

    }


    @Test
    @DisplayName("Engine evaluate nothing")
    fun testCtor()
    {
        val symbTab: SymbolTable = Engine.evaluate(listOf()).symbolTable
        assertEquals(0, symbTab.size)
    }





    @Test
    @DisplayName("TypeDef Engine test")
    fun engTestOne() {

        val colList = listOf("col1", "col2", "col3")
        val verbList = listOf(OpTypeDef("testType", colList ))
        val subj = Engine

        val symbTab :SymbolTable = Engine.evaluate(verbList).symbolTable

        assertEquals( 1, symbTab.size,"Should be one element")
        assertEquals(Type(colList),  symbTab.getTypeDef("testType"))
    }



    @Test
    @DisplayName("Engine Invalid Type Ref Test")
    fun engTestNoType()
    {
        val colList = listOf("col1", "colb", "colIII")
        val verbList : List<OpCode> = listOf(OpTypeDef("T1", colList),
                OpTypeRef("T2"))
        assertFailsWith<IllegalStateException>("Type T2, not  defined") {Engine.evaluate(verbList) }
    }


    @Test
    @DisplayName("Type Ref LC def UC ref")
    fun testTypeRefLC()
    {
        val colList = listOf("col1", "colb", "colc")
        val verbList : List<OpCode> = listOf(OpTypeDef("t1", colList),
                                             OpTypeDef("T2", listOf("foo", "bar", "BAZ")))
        val subj = Engine
        val symbTab :SymbolTable = subj.evaluate((verbList)).symbolTable

        assertEquals( 2, symbTab.size,"Should be two elements")
        assertEquals(Type(colList),  symbTab.getTypeDef("T1"))
    }

    @Test
    @DisplayName("Type ref UC def and ref")
    @Ignore
    fun engTestTwo()
    {
        val colList = listOf("this", "that", "the_other")
        val verbList : List<OpCode> = listOf(OpTypeDef("T1", listOf("name", "addr", "phone")),
                OpTypeDef("T2",  colList),
                OpTypeRef("T2"))

        val symbTab :SymbolTable = Engine.evaluate(verbList).symbolTable
        assertEquals( 2, symbTab.size,"Should be two elements")
        assertEquals(Type(colList),  symbTab.getTypeDef("T2"))
    }


    @Test
    @DisplayName("Engine Type Ref Test LC Ref")
    fun testTypeRefLC2()
    {
        val colList = listOf("col1", "colb", "colIII")
        val verbList : List<OpCode> = listOf(OpTypeDef("T1", colList),
                OpTypeRef("t1"))
        val symbTab :SymbolTable = Engine.evaluate(verbList).symbolTable

        assertEquals( 1, symbTab.size,"Should be one element")
        assertEquals(Type(colList),  symbTab.getTypeDef("t1"))
    }

    @Test
    @DisplayName("Header Test")
    fun engTestThree()
    {
        val colList = listOf("COL1", "colb", "COL3")
        val verbList : List<OpCode> = listOf(OpTypeDef("T1", colList),
                OpTypeRef("T1"),
                OpHeader())
        val actual = Engine.generate(verbList)

        assertEquals(1, actual.size)
        assertEquals("COL1|colb|COL3", actual[0] )
    }


    @Test
    @DisplayName("Header without type ref ")
    @Ignore
    fun engHdrNoTyp()
    {
        val colList = listOf("col1", "colb", "colIII")
        val verbList : List<OpCode> = listOf(OpTypeDef("T1", colList),
                                             OpHeader())
        assertFailsWith<IllegalStateException> {Engine.generate(verbList)}

    }

}
