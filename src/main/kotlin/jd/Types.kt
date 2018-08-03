package jd

sealed class SymbolValue()
data class Type( val fields : List<String> ) : SymbolValue()
{
}

data class State(var symbolTable: SymbolTable,
                 var curtype: Type,
                 var rec : Record,
                 var result : MutableList<String>)
