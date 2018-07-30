package jd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


object RecTest {
    @Test
    fun initialRecord() {
        val subj = Record(arrayOf("col1", "col2", "col3"))
        assertEquals( "COL1|COL2|COL3", subj.header())
        assertEquals("||", subj.values())
    }

    @Test
    fun valueCheck() {
        val subj = Record(arrayOf("col1", "col2", "col3"))
        subj["col1"] = "val1"
        assertEquals("val1", subj["col1"])

        subj["col3"] = "val3"
        assertEquals("val3", subj["col3"])

        assertEquals("", subj["col2"])
    }

    @Test
    fun getNonExistent() {
        val subj = Record(arrayOf("col1", "col2", "col3"))
        assertThrows<NoSuchItem> { subj.get("col0") }
    }

    @Test
    fun setNonExistent() {
        val subj = Record(arrayOf("col1", "col2", "col3"))
        assertThrows<NoSuchItem> { subj.set("col0", "A") }
    }
}