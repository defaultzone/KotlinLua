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

import luaCore.LocalVar
import luaCore.LuaNode

fun luaReturn(vararg elements : Any) : LuaNode {
    var statement : String = "return "
    for (item in elements) {
        statement += when (item) {
            is LocalVar -> item.read()
            is Argument -> item.read()
            is String   -> "[=[$item]=]"
            is Int, is Float, is Long, is Boolean, is Double -> item.toString()
            else -> throw IllegalArgumentException("Illegal value type: ${item.javaClass}")
        }
    }
    return LuaNode(statement)
}