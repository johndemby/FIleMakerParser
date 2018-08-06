package jd

typealias StringList = List<String>
//typealias MutableStringList = MutableList<String>

typealias PairOfStrings = Pair<String, String>

sealed class OpCode ( val lineNo : Int)
{

}

data class OpTypeDef ( private val line : Int, val name : String , val fields : StringList) : OpCode(line)
{
}

data class OpTypeRef( private val line : Int, val name : String) : OpCode(line)
{
}

data class OpRecord (private val line : Int)  : OpCode(line)
{
}

data class OpWrite(private val line : Int) : OpCode(line)
{
}

data class OpHeader(private val line : Int) : OpCode(line)
{
}

data class OpCopy( private val line : Int, val name : String) : OpCode(line)
{
}

data class OpSet(private val line : Int, val name :String , val x : String) : OpCode(line)
{
}








