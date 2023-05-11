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
import lua.node.LuaNode

private val ignoreCertainRegex : Array<Regex> = arrayOf(
    Regex("[a-zA-Z_][A-Za-z0-9_.]+\\(.*?\\)$"), // LUA.CORE.FUNCTION.FUNCTION
    Regex("_[0-1]+"),                           // LUA.CORE.LOCAL
    Regex("--\\[\\[u]].*"),                     // LUA.CORE.UNSAFE
    Regex("--\\[\\[-1]]_[0-1+]\\(.*?\\)$")
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

fun makeStatement(suffix : String, postfix : String, condition : String, conditionFunction : () -> Unit, useEndKeyword : Boolean) {
    val conditionOperators : Map<String, String> = mapOf(
        "!="    to " ~= ",
        "!=="   to " ~= ",
        "==="   to " == ",
        "&&"    to " and ",
        "||"    to " or ",
        "!"     to " not "
    )

    var replacedCondition = condition
    for ((key, value) in conditionOperators) replacedCondition = replacedCondition.replace(key, value)

    LuaNode("$suffix $replacedCondition $postfix")
    conditionFunction()
    if (useEndKeyword) LuaNode("end")
}