
package jd
import java.io.Writer


class Engine(val verbs : List<OpCode>) {

    private val symbolTable = SymbolTable()

    private val NOTYPE = Type(listOf())
    fun eval(writer : Writer = System.out.writer())
    {
        // initially only type defs and type refs are allowed

        var currentType : Type = NOTYPE
        var rec : Record = Record(currentType.fields.toTypedArray())
        for (v in verbs) {
            when (v) {
                is OpTypeDef -> symbolTable[v.name] = Type(v.fields)
                is OpTypeRef -> {
                    val q: SymbolValue = checkNotNull(symbolTable[v.name]) {
                        "Type ${v.name}, not  defined"
                    }
                    currentType = if (q is Type) q
                    else throw IllegalStateException("Symbol ${v.name} is not a TYPE")
                }

                is OpRecord -> rec = Record(currentType.fields.toTypedArray())
                is OpWrite -> println(rec.toString())
                is OpSet -> rec.set(v.name, v.x)

            }

        }

    }

 fun getSymbolTable() : SymbolTable {
    return symbolTable
 }

}
