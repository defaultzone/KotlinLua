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
 *      value {String|Int|Long|Boolean|Nothing(null)}: Value for variable, by default it's `nil`.
 * Variable name acts as unsigned 16 bit (limit: 0-65535).
 *
 * Functions:
 *      `fun read() : String;`
 *
 *      `fun change(value: Any) : LuaNode`
 *
 *      `fun toArgument() : Argument = if (accessToVar) Argument("___TO_ARGUMENT:${varName}___") else Argument("___TO_ARGUMENT:NULL___")`
 * @param value {String|Long|Int|Boolean|Float|Double|Table|null}
 */

class LocalVar(private var value : Any? = null) {
    private var accessToVar : Boolean = true
    private val varName : String = "_" + Data.currentItemNode.toString(2)

    /**
     * Get keys for a Lua table in a string.
     *
     * @param input {String}
     * @return {List<String>}
     */
    private fun getKeys(input : String) : List<String> =
        Regex("\\[[^\\[\\]]*]|\\w+").findAll(input).map { it.value.trim('[', ']') }.toList()

    init {
        if (Data.currentItemNode.toUInt() != 65535u) {
            if (value != null) {
                LuaNode(
                    "local $varName = ${
                        when (value) {
                            is String -> {
                                if ((value as String).matches(Regex("_[0-1]+\\(.*?\\)$"))) {
                                    value
                                } else {
                                    "[=[$value]=]"
                                }
                            }
                            is Long, is Int, is Boolean, is Float, is Double -> value
                            is Table -> (value as Table).readAsLuaTable()
                            else -> throw IllegalArgumentException("Illegal value type: ${value!!.javaClass}")
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

    /**
     * Get name of variable as `_${{ bit name of variable }}` only if accessToVar. Otherwise, it will return `null`.
     */

    fun read() : String? = if (accessToVar) varName else null

    /**
     * Convert variable to function argument.
     * Returns `___TO_ARGUMENT:__{{ bit name of variable }}___` only if the variable is accessed.
     * Otherwise, it will return `___TO_ARGUMENT:NULL___`.
     */
    fun toArgument() : Argument = if (accessToVar) Argument("___TO_ARGUMENT:${varName}___") else Argument("___TO_ARGUMENT:NULL___")

    /**
     * Change the value of a variable.
     *
     * @param newValue {String|Long|Int|Boolean|Float|Double|null}
     * @return {LuaNode}
     */
    fun change(newValue : Any?) : LuaNode {
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

    /**
     * Read table value by key. Usage example:
     *
     * ```kotlin
     * readTableItem("key[1][false]['key']")    // Will return `["key"][1][false]["key"]`.
     * readTableItem("[key][1][false]['key']")  // Will return `["key"][1][false]["key"]`.
     * ```
     *
     * @param key {String|Int|Float|Long|Boolean|*(will return "nil")}
     * @return {String}
     */

    fun readTableItem(key : Any?) : String {
        return when (key) {
            is String -> {
                var output = ""
                getKeys(key).forEach {
                    val tableItemMatchResult : MatchResult? = Regex("TABLE_ITEM(.*)").find(it)
                    output += if (tableItemMatchResult == null) {
                        if (it.matches(Regex("[0-9]+|true|false"))) {
                            "[$it]"
                        } else if (it.matches(Regex("[\"'`].*[\"'`]"))) {
                            "[\"" + it.substring(1, it.length - 1) + "\"]"
                        } else {
                            "[\"$it\"]"
                        }
                    } else {
                        "[${tableItemMatchResult.groupValues[1]}]"
                    }
                }
                varName + output
            }
            is Int, is Float, is Long, is Boolean -> "$varName[$key]"
            else -> "nil"
        }
    }

    /**
     * Allows you to change the value of the table through a local variable.
     *
     * @param key {String}
     * @param value {String|Long|Int|Boolean|Float|Double|Table|null}
     */

    fun tableChange(key : String, value : Any? = null) : LuaNode {
        return LuaNode("${this.read()}[\"$key\"]=${when (value) {
            is String -> "[=[$value]=]"
            is Long, is Int, is Boolean, is Float, is Double -> value
            is Table -> value.readAsLuaTable()
            else -> "nil"
        }}")
    }

    /**
     * Get the length of the table row. Returns `#_{{ bit variable name }}`
     */
    fun length() : String = "#$varName"

    /**
     * Get type of `value` argument.
     */
    fun type() : String = value!!::class.simpleName!!
}
