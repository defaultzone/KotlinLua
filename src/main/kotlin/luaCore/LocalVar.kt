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

import luaCore.funcOperations.Argument

/**
 * Usage: `val someVar = LocalVar(value)`, where:
 *      value {String|Int|Long|Boolean|Nothing(null)}: Value for variable, by default it's `NullPointerException` or `nil`.
 * Variable name acts as unsigned 16 bit (limit: 0-65535).
 *
 * Functions:
 *      fun read() : String;
 *      fun change(value: Any) : LuaNode
 *      fun toArgument() : Argument = if (accessToVar) Argument("___TO_ARGUMENT:${varName}___") else Argument("___TO_ARGUMENT:NULL___")
 * @param value {String|Long|Int|Boolean|Float|Double|Table|null}
 */

class LocalVar(private var value: Any? = null) {
    private var accessToVar : Boolean = true
    private val varName : String = "_" + Data.currentItemNode.toString(2)

    init {
        if (Data.currentItemNode.toUInt() != 65535u) {
            if (value != null) {
                LuaNode(
                    "local $varName = ${
                        when (value) {
                            is String -> "[=[$value]=]"
                            is Long, is Int, is Boolean, is Float, is Double -> value
                            is Table -> (value as Table).readAsLuaTable()
                            else -> {
                                println("""
                                    [ warning ]: Local variable "$varName" has unknown value: ${value!!::class.simpleName}.
                                    [ info ]: Acceptable value-types: String, Long, Int, Float, Double, Boolean, or just `null`.
                                """.trimIndent())
                                "nil"
                            }
                        }
                    }"
                )
            } else {
                LuaNode("local $varName = nil")
            }

            Data.currentItemNode++
        } else {
            println("[ warning ]: Data.currentItemNode overflow. Max length: from 0u to 65535u")
            accessToVar = false
        }
    }

    fun read() : String? = if (accessToVar) varName else null
    fun toArgument() : Argument = if (accessToVar) Argument("___TO_ARGUMENT:${varName}___") else Argument("___TO_ARGUMENT:NULL___")

    fun change(newValue: Any?) : LuaNode {
        if (accessToVar) {
            value = newValue
            if (newValue != null) {
                return LuaNode("$varName = " + when (newValue) {
                        is String -> "[=[$newValue]=]"
                        is Long, is Int, is Boolean, is Float, is Double -> newValue
                        is Table -> newValue.readAsLuaTable()
                        else -> {
                            println("""
                                [ warning ]: Cannot change variable value. Reason: unknown value-type(${newValue::class.simpleName})
                                [ info ]: Acceptable value-types: String, Long, Int, Float, Double, Boolean, or just `null`.
                            """.trimIndent())
                            "nil"
                        }
                    }
                )
            } else {
                return LuaNode("$varName = nil")
            }
        }

        return LuaNode("NULL_LUA_NODE")
    }
}