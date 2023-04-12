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
import lua.core.Table
import lua.core.makeParam

/**
 * Lua function.
 *
 * Registers a Lua function whose name is an underscore plus a binary code (see node/Data.kt).
 * The actual code to be executed is `function : (List<Argument>) -> Unit`.
 * You can access function arguments with `it` key.
 *
 * Functions:
 *      Executes the function as `function(arguments)`. Returns a string (or null if warning received).
 *      `fun execute(vararg arguments : Any?) : String?`.
 *
 * For example see `samples/Functions/Names.kt`.
 *
 * @param arguments {Argument}
 * @param function  {(List<Argument>) -> Unit)}
 */

class Function(vararg arguments : Argument, function : (List<Argument>) -> Return) {
    private val varName : String = Data.currentItemNode.toString(2)
    private var returnedParams : Return? = null

    init {
        Data.currentItemNode++
        LuaNode("function _$varName(${
            arguments.joinToString(",") { it.read() }
        })")
        arguments.forEach { it.insertNodeWithValue() }
        returnedParams = function(arguments.toList())
        LuaNode("end")
    }

    fun execute(vararg arguments : Any?) : CastFunction = CastFunction("_$varName(${arguments.joinToString { makeParam(it) }})", returnedParams!!)
}
