package jd


typealias StringList = List<String>

sealed class OpCode

data class OpTypeDef (val name : String , val fields : StringList) : OpCode()
{
    fun execute()
    {
        println("typedef $name fields: $fields")
    }

}

data class OpTypeRef(val name : String) : OpCode()


data class OpRecord (val name : String , val fields : StringList)  : OpCode()



class tryIt
{


    fun doSomething(): OpCode
    {

        val op: OpCode = OpTypeDef("ralph", listOf("A", ":B", "Q"))

        return op

    }
}



