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

package lua.descriptors

import lua.core.DescriptorObject
import lua.core.unsafe

object Basic {
    fun getDescriptorArray(optimize : Boolean = false, key : String? = null) : Array<DescriptorObject> {
        val descriptorMap : Map<String?, Array<DescriptorObject>> = mapOf(
            null        to arrayOf(
                // TODO: Descriptor for _G, _VERSION;
                DescriptorObject("assert", 1, optimize = optimize),
                DescriptorObject("collectgarbage", 0, optimize = optimize),
                DescriptorObject("dofile", 0, optimize = optimize),
                DescriptorObject("error", 0, optimize = optimize),
                DescriptorObject("getfenv", 1, optimize = optimize),
                DescriptorObject("getmetatable", 1, optimize = optimize),
                DescriptorObject("ipairs", 3, optimize = optimize),
                DescriptorObject("load", 1, optimize = optimize),
                DescriptorObject("loadfile", 1, optimize = optimize),
                DescriptorObject("loadstring", 1, optimize = optimize),
                DescriptorObject("module", 0, optimize = optimize),
                DescriptorObject("next", 2, optimize = optimize),
                DescriptorObject("pairs", 3, optimize = optimize),
                DescriptorObject("pcall", 1, optimize = optimize),
                DescriptorObject("print", 0, optimize = optimize),
                DescriptorObject("rawequal", 1, optimize = optimize),
                DescriptorObject("rawget", 1, optimize = optimize),
                DescriptorObject("rawset", 0, optimize = optimize),
                DescriptorObject("require", 1, optimize = optimize) { params, name ->
                    if (params.size == 2 && params[0] is String && params[1] is Boolean) {
                        val path : String       = params[0] as String
                        val unsafe : Boolean    = params[1] as Boolean
                        if (unsafe) return@DescriptorObject "$name([=[${params[0]}]=])".unsafe()
                        else        return@DescriptorObject "$name([=[${params[0]}]=])"
                    } else throw IllegalArgumentException("`require` usage: descriptor[\"require\"](path: String, unsafe: Boolean)")
                },
                DescriptorObject("select", 1, optimize = optimize),
                DescriptorObject("setfenv", 0, optimize = optimize),
                DescriptorObject("setmetatable", 1, optimize = optimize /* TODO: Metatable implementation. */),
                DescriptorObject("tonumber", 1, optimize = optimize),
                DescriptorObject("tostring", 1, optimize = optimize),
                DescriptorObject("type", 1, optimize = optimize),
                DescriptorObject("unpack", 0, optimize = optimize),
                DescriptorObject("xpcall", 1, optimize = optimize)
            ),
            "math"      to arrayOf(),
            "coroutine" to arrayOf(),
            "debug"     to arrayOf(),
            "os"        to arrayOf(),
            "io"        to arrayOf(),
            "string"    to arrayOf(),
            "package"   to arrayOf(),
            "string"    to arrayOf(),
            "table"     to arrayOf()
        )
        return descriptorMap[key]!!
    }
}