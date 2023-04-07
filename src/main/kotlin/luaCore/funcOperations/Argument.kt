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


package luaCore.funcOperations

import luaCore.Data
import luaCore.LuaNode
import luaCore.Table

/**
 * Argument for functions.
 *
 * Allows you to use the argument (s) in the function (s), acts as a class.
 * Functions:
 *      Insert LuaNode with default value. Example: local {arg_name} = {arg_name} or {user_value}.
 *          `fun insertNodeWithValue() : LuaNode?`;
 *      Read argument value as String, also can return `null`, if overflow(UShort) or unknown type.
 *          `fun readValue() : String? = if (accessToArgument) itemName else null`;
 * See `samples/Functions/Main.kt` for more information.
 * @param value {String|Long|Int|Boolean|Float|Double|Table|null}
 */

class Argument(private var value : Any? = null) {
    private var accessToArgument = true
    private val itemName : String = "_" + Data.currentItemNode.toString(2)

    init {
        if (value != null) {
            if (Data.currentItemNode.toUInt() != 65535u) {
                value = when (value) {
                    is String -> {
                        if (value != "___TO_ARGUMENT:NULL___") {
                            val toArgumentMatchResult: MatchResult? =
                                Regex("___TO_ARGUMENT:(_[0-1]+)___").find(value as String)
                            if (toArgumentMatchResult != null) {
                                toArgumentMatchResult.groupValues[1]
                            } else {
                                "[=[$value]=]"
                            }
                        } else {
                            accessToArgument = false
                        }
                    }
                    is Long, is Int, is Boolean, is Float, is Double -> value
                    is Table -> (value as Table).readAsLuaTable()
                    else -> {
                        println("[ warning ]: Argument `$itemName` has unknown value-type.")
                        println("[ info ]: Acceptable value-types: String, Long, Int, Float, Double, Boolean, or just `null`.")
                        accessToArgument = false
                        "nil"
                    }
                }
            } else {
                accessToArgument = false
                println("[ warning ]: Data.currentItemNode overflow. Max length: from 0u to 65535u")
            }
        }

        if (accessToArgument) Data.currentItemNode++
    }

    // Insert LuaNode with default value. Example: local {arg_name} = {arg_name} or {user_value}.
    fun insertNodeWithValue() : LuaNode? {
        if (accessToArgument) {
            if (value != null) {
                LuaNode("local $itemName = $itemName or $value")
            }
        }

        return null
    }

    // Read argument value as String.
    fun readValue() : String = if (accessToArgument) itemName else "nil"
}
