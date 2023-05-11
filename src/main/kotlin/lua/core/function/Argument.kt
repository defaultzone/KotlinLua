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


package lua.core.function

import lua.node.Data
import lua.node.LuaNode
import lua.core.makeParam

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
    private val itemName : String = "_" + Data.currentItemNode.toString(0)
    init {
        if (value != null) {
            value = when (value) {
                is String -> {
                    val toArgumentMatchResult : MatchResult? = Regex("___TO_ARGUMENT:(_[0-1]+)___").find(value as String)
                    // RETURN:
                    if (toArgumentMatchResult != null)
                        toArgumentMatchResult.groupValues[1]
                    else
                        "[=[$value]=]"
                }
                else -> makeParam(value)
            }
        }

        Data.currentItemNode++
    }

    // Insert LuaNode with default value. Example: local {arg_name} = {arg_name} or {user_value}.
    fun insertNodeWithValue() : LuaNode =
        LuaNode("local $itemName = $itemName or $value".takeIf { value != null } ?: "NULL_LUA_NODE")

    /**
     * Swap value node with value as passed by `newValue`, but only if was used `insertNodeWithValue()`.
     * @param newValue {String|LocalVar|Argument|Table|Int|Float|Long|Boolean|null}
     * @return {LuaNode}
     */
    fun change(newValue : Any?) : LuaNode =
        LuaNode("$itemName = ${makeParam(newValue)}".takeIf { value != null } ?: "NULL_LUA_NODE")

    // Read argument value as String.
    fun read() : String = itemName
}
