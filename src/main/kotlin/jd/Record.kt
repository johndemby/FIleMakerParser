package jd

data class NoSuchItem(val name : String) : IllegalArgumentException(name)

class Record(val fields : Array<String>)
{
    private val valMap : MutableMap<String, String> = LinkedHashMap()
    init{
        for (s in fields) { valMap.put(s.toUpperCase(), "") }
    }

    operator fun get(name : String) : String =
            valMap.getOrElse(name.toUpperCase()) {throw NoSuchItem(name)}

    operator fun set(name : String, value : String) {
        val nm = name.toUpperCase()

        if (valMap.containsKey(nm))
            valMap[nm] = value
        else
            throw NoSuchItem(name)
    }

    fun fieldExists(name : String) : Boolean = valMap.containsKey(name)

    fun values() : String = valMap.values.joinToString("|")
    fun header() : String = valMap.keys.joinToString("|") {it.toUpperCase() }
}




