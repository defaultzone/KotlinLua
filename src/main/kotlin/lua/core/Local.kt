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

package lua.core

import lua.core.function.Argument
import lua.core.function.Return
import lua.node.Data
import lua.node.LuaNode

/**
 * Register local variable and add this in `Data.fileContent`.
 *
 * Usage: `val someVar = LocalVar(value)`, where:
 *      value {String|Int|Long|Boolean|Nothing(null)}: Value for variable, by default it's `nil`.
 * @param value {String|Long|Int|Boolean|Float|Double|Table|null}
 */

class Local(private var value : Any? = null) {
    private var variableNames : Array<String> = emptyArray() // Used when (value is Return).
    private val varName : String = "_" + Data.currentItemNode.toString()

    /**
     * Get keys for a Lua table in a string.
     *
     * @param input {String}
     * @return {List<String>}
     */
    private fun getKeys(input : String) : List<String> =
        Regex("\\[[^\\[\\]]*]|\\w+").findAll(input).map { it.value.trim('[', ']') }.toList()

    init {
        when (value) {
            is String -> {
                val functionMatchResult : MatchResult? = Regex("^--\\[\\[([- 0-9]+)]][a-zA-Z_][A-Za-z0-9_.]+\\(.*?\\)$").find((value as String))
                if (functionMatchResult != null) {
                    val functionReturnedLength : Int = functionMatchResult.groupValues[1].toInt().takeIf { it != -1 } ?: 1
                    value = Return(Array(functionReturnedLength) { -1 }, useInitPart = { false })
                    for (i in 1..functionReturnedLength) {
                        variableNames += arrayOf("_" + Data.currentItemNode.toString())
                        Data.currentItemNode++
                    }
                    LuaNode("local ${variableNames.joinToString(" , ")} = ${functionMatchResult.value}")
                } else {
                    LuaNode("local $varName = ${makeParam(value)}")
                    Data.currentItemNode++
                }
            }
            else -> {
                LuaNode("local $varName = ${makeParam(value)}")
                Data.currentItemNode++
            }
        }
    }

    /**
     * Get name of variable as `_${{ bit name of variable }}` only if accessToVar. Otherwise, it will return `null`.
     */

    fun read() : String = varName

    /**
     * Convert variable to function argument.
     * Returns `___TO_ARGUMENT:__{{ bit name of variable }}___`.
     */
    fun toArgument() : Argument = Argument("___TO_ARGUMENT:${varName}___")

    /**
     * Change the value of a variable.
     *
     * @param newValue {String|Long|Int|Boolean|Float|Double|null}
     * @return {LuaNode}
     */
    fun change(newValue : Any?) : LuaNode =
        LuaNode("$varName = ${makeParam(newValue)}".takeIf { value != null } ?: "NULL_LUA_NODE")

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
     * Get the length of the table row. Returns `#_{{ bit variable name }}`
     */
    fun length() : String = " #$varName "

    /**
     * Get type of `value` argument.
     */
    fun type() : String = value!!::class.simpleName!!

    operator fun get(i : Int) : String = when (i) {
        0 -> varName
        in 1..variableNames.size -> {
            if (value is Return)
                variableNames[i]
            else
                throw IllegalAccessError("Can't get name using int i: $i")
        }
        else -> throw IllegalAccessError("Can't get name using int i: $i")
    }

    operator fun set(key : Any, value : Any) : LuaNode =
        LuaNode("${this.read()}[ ${makeParam(key)} ]=${makeParam(value)}")
}
