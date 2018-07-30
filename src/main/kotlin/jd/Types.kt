package jd

sealed class SymbolValue()
data class Type( val fields : List<String> ) : SymbolValue()
{
}

