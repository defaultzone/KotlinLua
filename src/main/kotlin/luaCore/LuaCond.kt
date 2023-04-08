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

object LuaCond {
    private fun makeStatement(keyword : String, condition : String, conditionFunction : () -> Unit, useEndKeyword : Boolean) {
        val conditionOperators : Map<String, String> = mapOf(
            "!="    to "~=",
            "!=="   to "~=",
            "==="   to "==",
            "&&"    to "and",
            "||"    to "or",
            "!"     to "not"
        )

        var replacedCondition = condition
        for ((key, value) in conditionOperators) {
            replacedCondition = replacedCondition.replace(key, value)
        }

        LuaNode("$keyword $replacedCondition then")
        conditionFunction()
        if (useEndKeyword) LuaNode("end")
    }

    fun If(condition : String, useEndKeyword : Boolean = true, invoke : () -> Unit) =
        makeStatement("if", condition, invoke, useEndKeyword)
    fun ElseIf(condition : String, useEndKeyword : Boolean = false, invoke : () -> Unit) =
        makeStatement("elseif", condition, invoke, useEndKeyword)
    fun Else(useEndKeyword : Boolean = false, invoke : () -> Unit) {
        LuaNode("else")
        invoke()
        if (useEndKeyword) LuaNode("end")
    }
    fun End() : LuaNode = LuaNode("end")
}