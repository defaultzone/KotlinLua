package lua.core

import lua.core.function.Argument

private val ignoreCertainRegex : Array<Regex> = arrayOf(
    Regex("_[0-1]+\\(.*?\\)$"), // LUA.CORE.FUNCTION.FUNCTION
    Regex("_[0-1]+")            // LUA.CORE.LOCAL
)

private fun makeStringParam(param : String) : String {
    for (regex in ignoreCertainRegex) {
        if (param.matches(regex)) return param
    }
    return "[=[$param]=]"
}

fun makeParam(param : Any?) : String = when (param) {
    is String -> makeStringParam(param)
    is Table -> param.readAsLuaTable()!!
    is Argument -> param.read()
    is Local -> param.read()
    is Int, is Boolean, is Float, is Double, is Long, is Short -> param.toString()
    null -> "nil"
    else -> throw IllegalArgumentException("Illegal argument-type: ${param.javaClass}")
}
