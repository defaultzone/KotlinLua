/**
 *  Free and Open Source library for building Lua scripts, using Kotlin. (KotlinLua)
 *  Copyright (C) 2023 defaultzon3
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *  USA
 */

package luaCore

import java.lang.NullPointerException

/**
 * Usage: `val someVar = LocalVar(varName, value)`, where:
 *      varName {String}: Name for variable, should match the following pattern: `[A-Za-z_][A-Za-z0-9_]+`
 *      value {String|Int|Long|Boolean|Nothing(null)}: Value for variable, by default it's `NullPointerException` or `nil`.
 * Functions:
 *      fun read() : String;
 *      fun change(value: Any) : LuaNode
 * @param varName {String}
 * @param value {String|Int|Long|Boolean|Nothing(null)}
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