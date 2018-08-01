package jd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.Ignore
import java.io.StringWriter
import kotlin.test.assertFailsWith


class EngineTest {

    @Test
    @DisplayName("Engine Basic Construction")
    fun testCtor()
    {
        val subj = Engine(emptyList())
        val symbTab: SymbolTable = subj.getSymbolTable()
        assertEquals(0, symbTab.size)
    }

    @Test
    @DisplayName("TypeDef Engine test")
    fun engTestOne() {

        val colList = listOf("col1", "col2", "col3")
        val verbList = listOf(OpTypeDef("testType", colList ))
        val subj = Engine(verbList)
        subj.eval()
        val symbTab :SymbolTable = subj.getSymbolTable()

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
        val subj = Engine(verbList)

        assertFailsWith<IllegalStateException>("Type T2, not  defined") {subj.eval() }
    }


    @Test
    @DisplayName("Type Ref LC def UC ref")
    fun testTypeRefLC()
    {
        val colList = listOf("col1", "colb", "colc")
        val verbList : List<OpCode> = listOf(OpTypeDef("t1", colList),
                                             OpTypeDef("T2", listOf("foo", "bar", "BAZ")))
        val subj = Engine(verbList)
        subj.eval()
        val symbTab :SymbolTable = subj.getSymbolTable()

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
        val subj = Engine(verbList)
        val symbTab :SymbolTable = subj.getSymbolTable()

        subj.eval()

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
        val subj = Engine(verbList)
        subj.eval()
        val symbTab :SymbolTable = subj.getSymbolTable()

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
        var myWriter = StringWriter()
        val subj = Engine(verbList)
        subj.eval(myWriter)

        assertEquals("COL1|colb|COL3", myWriter.toString() )
    }


    @Test
    @DisplayName("Header without type ref ")
    @Ignore
    fun engHdrNoTyp()
    {
        val colList = listOf("col1", "colb", "colIII")
        val verbList : List<OpCode> = listOf(OpTypeDef("T1", colList),
                                             OpHeader())
        val subj = Engine(verbList)
        val symbTab :SymbolTable = subj.getSymbolTable()

        assertFailsWith<IllegalStateException> {subj.eval()}

    }



}