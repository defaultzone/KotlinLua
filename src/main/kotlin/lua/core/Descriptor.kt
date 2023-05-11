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

import lua.node.Data
import kotlin.collections.MutableMap as MutableMap

data class DescriptorObject(
    val value : String,
    val returnedParams : Int,
    val type : Int = Descriptor.FUNCTION,
    val requirePath : String = "INCLUDED",
    val optimize : Boolean = true,
)

class RegisteredDescriptor(private val key : String?, private val returnedParams : Int) {
    operator fun invoke(vararg params : Any?) : String = "--[[${when {
            returnedParams <= 0 -> -1
            else -> returnedParams
        }}]]$key(${params.joinToString(" , ") { param -> makeParam(param) }})".takeIf {
            key != null
        } ?: "NULL_LUA_NODE"
}

class Descriptor(private val invoke : (() -> Array<DescriptorObject>)? = null) {
    private var optimizedFunctions : MutableMap<Int, String> = mutableMapOf()
    private var currentTableKey : Int = 0

    private fun MutableMap<Int, String>.joinToString(
        separator : String = ",", invoke : (key : Int, value : String) -> String
    ) : String {
        var output = ""
        this.forEach { map -> output += invoke(map.key, map.value) + "," }
        return output.substring(0, output.length - separator.length) // Remove last `separator` in String.
    }

    private fun <K, V> Map<K, V>.getKeyByValue(value: V): K? = this.entries.firstOrNull { it.value == value }?.key

    companion object {
        const val FUNCTION : Int = 0
        const val VARIABLE : Int = 1
    }

    init {
        invoke!!().forEach {
            if (it.optimize) optimizedFunctions[currentTableKey++] = it.value
        }

        Data.fileHeader = "local _0={${optimizedFunctions.joinToString { key, value ->
            "_${key.toString(2)}=$value"
        }}}"
    }

    operator fun get(key : String) : RegisteredDescriptor {
        invoke!!().forEach {
            if (it.value == key)
                return if (it.optimize)
                    RegisteredDescriptor(
                        "_0._" + optimizedFunctions.getKeyByValue(key)!!.toString(2),
                        it.returnedParams
                    )
                else
                    RegisteredDescriptor(key, it.returnedParams)
        }
        return RegisteredDescriptor(null, -1)
    }
}