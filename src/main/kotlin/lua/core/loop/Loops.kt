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

package lua.core.loop

import lua.core.makeStatement
import lua.node.LuaNode

/**
 * `break` in Lua as LuaNode.
 */
fun breakLoop() : LuaNode = LuaNode("break")

/**
 * Registering a Lua "while" loop. Accepts a string as a condition or `true/false`.
 *
 * @param condition {String|Boolean}
 * @param loopContent {() -> Unit}
 */
fun whileLoop(condition : Any, loopContent : () -> Unit) =
    makeStatement("while", "do", when (condition) {
        is String   -> condition
        is Boolean  -> condition.toString()
        else        -> "nil"
    }, loopContent, true)