package jd

data class RecordDef (val name : String, val fields : List<String>)

enum class DataType { DTString, DTTypeDef, DTRecord, DTFldList }


data class Entry (val name : String, val type: DataType, val value : Any);

class SymbolTable
{

    fun getTypeDef (name : String) : Type = get(name) as Type

    operator fun get(name:String) : SymbolValue? = dict[name.toUpperCase()]
    operator fun set(name:String, symbValue : SymbolValue)
    {
        dict[name.toUpperCase()] = symbValue
    }

/*

    fun setString (name: String, value: String) {
        dict[name.toUpperCase()] = value;
    }

    fun getString (name : String ) = dict[name.toUpperCase()] as String
*/

    private val dict : HashMap<String, SymbolValue>  = HashMap()

    val size : Int
       get() = dict.size



}