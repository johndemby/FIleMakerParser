
package jd

object Engine
{
    val NOTYPE = Type(listOf())

    val sep = System.getProperty("FIELD_SEP") ?: "|"

    private fun OpHeader.eval(state: State): State
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

    private fun OpTypeDef.eval(state: State): State
    {
        state.symbolTable[name] = Type(fields)
        return state
    }

    private fun OpTypeRef.eval(state: State): State
    {

        with(state)
        {
            val q: SymbolValue = checkNotNull(symbolTable[name]) { "Type $name, not  defined" }
            if (q is Type)
                curtype = q
            else
                throw IllegalStateException("Symbol $name is not a TYPE")
        }
        return state
    }

    private fun OpRecord.eval(state: State): State
    {
        state.rec = Record(state.curtype.fields.toTypedArray())
        return state
    }

    private fun OpSet.eval(state: State): State
    {
        state.rec[name] = x
        return state
    }

    private fun OpWrite.eval(state: State): State
    {
        state.result.add(state.rec.toString())
        return state
    }


    private fun OpCopy.eval(state: State): State
    {
        return state.also { System.err.println("Copy not impemented") }
    }


    /**
     *  Process the list of opcodes and produce State instance.
     *
     *  @param verbs  parsed instructions
     *  @return an instance of [State] resulting from evaluating the opcodes.
     */
    fun evaluate(verbs: List<OpCode>) : State
    {
        val rec = Record(NOTYPE.fields.toTypedArray())
        val res: MutableList<String> = mutableListOf()
        val state = State(SymbolTable(), NOTYPE, rec, res)

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


        return verbs.fold(state) { s, op -> eval(s, op) }
    }

    /**
     * Generate a string list based on the supplied list of opcodes.
     *
     * @param verbs  parsed instructions
     * @return List of Strings each element represents a line of text.
     */
    fun generate(verbs: List<OpCode>): StringList  = evaluate(verbs).result

}




