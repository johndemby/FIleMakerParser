
package jd
import java.io.PrintWriter
import java.io.Writer

val sep = "|"
val NOTYPE = Type(listOf())

object Engine
{

    fun OpHeader.eval(state: State): State
    {
        with(state) {
            if (curtype == NOTYPE)
            {
                throw IllegalStateException("NO TYPE DEFINED")
            }
            else
            {
                result.add(curtype.fields.joinToString(sep))
            }
        }
        return state
    }

    fun OpTypeDef.eval(state: State): State
    {
        state.symbolTable[name] = Type(fields)
        return state
    }

    fun OpTypeRef.eval(state: State): State
    {

        with(state)
        {
            val q: SymbolValue = checkNotNull(symbolTable[name]) { "Type ${name}, not  defined" }
            if (q is Type)
                curtype = q
            else
                throw IllegalStateException("Symbol ${name} is not a TYPE")
        }
        return state
    }

    fun OpRecord.eval(state: State): State
    {
        state.rec = Record(state.curtype.fields.toTypedArray())
        return state
    }

    fun OpSet.eval(state: State): State
    {
        state.rec.set(name, x)
        return state
    }

    fun OpWrite.eval(state: State): State
    {
        state.result.add(state.rec.toString())
        return state
    }



    fun OpCopy.eval(state: State): State
    {
        return state.also { System.err.println("Copy not impemented") }
    }


    fun evaluate(verbs: List<OpCode>) : State
    {
        val sep = "|"

        fun eval(state: State, opCode: OpCode): State =
                when (opCode)
                {
                    is OpTypeDef -> opCode.eval(state)
                    is OpTypeRef -> opCode.eval(state)

                    is OpHeader -> opCode.eval(state)

                    is OpRecord -> opCode.eval(state)
                    is OpWrite -> opCode.eval(state)
                    is OpSet -> opCode.eval(state)
                    is OpCopy -> state
                }

        val NOTYPE = Type(listOf())
        val rec = Record(NOTYPE.fields.toTypedArray())
        val res: MutableList<String> = mutableListOf()
        val state = State(SymbolTable(), NOTYPE, rec, res)

        return verbs.fold(state, { s, op -> eval(s, op) })
    }

    fun generate(verbs: List<OpCode>): StringList  = evaluate(verbs).result

}

class Engine2(val verbs : List<OpCode>) {


    private val symbolTable = SymbolTable()
    final private val sep : String = "|"
    private val NOTYPE = Type(listOf())
    fun eval(writer : Writer = System.out.writer())
    {
        // initially only type defs and type refs are allowed
        val wrt = PrintWriter(writer, true)
        var currentType : Type = NOTYPE
        var rec : Record = Record(currentType.fields.toTypedArray())
        for (v in verbs) {
            when (v) {
                is OpTypeDef -> symbolTable[v.name] = Type(v.fields)
                is OpTypeRef -> {
                    val q: SymbolValue = checkNotNull(symbolTable[v.name]) { "Type ${v.name}, not  defined" }
                    if (q is Type)
                        currentType =  q
                    else
                        throw IllegalStateException("Symbol ${v.name} is not a TYPE")
                }

                is OpHeader ->
                    if (currentType == NOTYPE)
                    {
                        throw IllegalStateException("NO TYPE DEFINED")
                    }
                    else
                    {
                        wrt.write(currentType.fields.joinToString(sep))
                    }

                is OpRecord -> rec = Record(currentType.fields.toTypedArray())
                is OpWrite -> println(rec.toString())
                is OpSet -> rec.set(v.name, v.x)

            }

        }
        wrt.flush()

    }

 fun getSymbolTable() : SymbolTable {
    return symbolTable
 }

}
