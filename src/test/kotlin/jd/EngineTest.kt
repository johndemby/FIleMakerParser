package jd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.Ignore
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
    @DisplayName("Engine Type Ref Test UC")
    fun engTestThree()
    {
        val colList = listOf("col1", "colb", "colIII")
        val verbList : List<OpCode> = listOf(OpTypeDef("T1", colList),
                OpTypeRef("T1"))
        val subj = Engine(verbList)
        subj.eval()
    }

    @Test
    @DisplayName("Engine Type Ref Test LC type")
    fun testTypeRefLC()
    {
        val colList = listOf("col1", "colb", "colIII")
        val verbList : List<OpCode> = listOf(OpTypeDef("t1", colList),
                OpTypeRef("T1"))
        val subj = Engine(verbList)
        subj.eval()
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
    }



    @Test
    @DisplayName("Header Test")
    @Ignore
    fun engTestTwo()
    {
        val colList = listOf("col1", "colb", "colIII")
        val verbList : List<OpCode> = listOf(OpTypeDef("T1", colList),
                                             OpHeader())
        val subj = Engine(verbList)

    }


}