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

/**
 * Adds a string to the array (which is then assembled into a file). Used to avoid problems with the String type.
 * Use "NULL_LUA_NODE" as content param to not add a line to the built file.
 * Functions:
 *      override fun toString() : String = content
 * @param content {String}
 */

class LuaNode(private val content : String?) {
    init {
        if (content != "NULL_LUA_NODE") Data.fileContent += arrayOf(content!!)
    }

    override fun toString() : String = content!!
}