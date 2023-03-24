package luaCore

import java.lang.NullPointerException

/**
 * @param varName
 * @param value {String | Number | Boolean | Nothing}
 */
class LocalVar(private val varName : String, value : Any = NullPointerException()) {
    private var accessToVar : Boolean = true

    init {
        if (varName.matches(Regex("[A-Za-z_][A-Za-z0-9_]+"))) {
            if (varName !in Data.usedVariables) {
                LuaNode(
                    "local $varName = ${
                        when (value::class.simpleName) {
                            "String" -> "[=[$value]=]"
                            "Long", "Int", "Boolean" -> value
                            "Nothing" -> "nil"
                            else -> {
                                println("""
                                   [ warning ]: Local variable "$varName" has unknown value: ${value::class.simpleName}.
                                   [ info ]: Acceptable value of variable types: String, Long, Int, Boolean, Nothing(null).
                                """.trimIndent())
                                "nil"
                            }
                        }
                    }"
                )
                Data.usedVariables[varName] = value
            } else {
                println("[ warning ]: Local variable $varName already registered.")
                accessToVar = false
            }
        } else {
            println("[ error ]: Unacceptable local variable name: $varName")
            accessToVar = false
        }
    }

    fun read() : String = if (accessToVar) varName else ""

    fun change(value: Any) : LuaNode {
        if (accessToVar) {
            return LuaNode(
                "$varName = " +
                when (value::class.simpleName) {
                    "String" -> "[=[$value]=]"
                    "Long", "Int", "Boolean" -> value
                    "Nothing" -> "nil"
                    else -> {
                        println("""
                            [ warning ]: Cannot change variable value. Reason: unknown value-type(${value::class.simpleName})
                            [ info ]: Acceptable value of variable types: String, Long, Int, Boolean, Nothing(null).
                        """.trimIndent())
                        "nil"
                    }
                }
            )
        }

        return LuaNode("NULL_LUA_NODE")
    }
}