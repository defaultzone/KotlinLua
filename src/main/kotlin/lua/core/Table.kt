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

import org.json.JSONArray
import org.json.JSONObject
import org.yaml.snakeyaml.Yaml

/**
 * Translates **JSON** or **YAML** into a Lua table.
 *
 * Public functions:
 *
 * Read class as Lua table, returns string or `null` if no "table access":
 * **` fun readAsLuaTable() : String?`**
 *
 * @param yaml {String|null}
 * @param json {String|null}
 * @throws IllegalArgumentException when `yaml` == null && `json` == null or `yaml` != null && `json` != null
 */

class Table(private val yaml : String? = null, private val json : String? = null) {
    private var accessToTable : Boolean = false

    init {
        accessToTable = when {
            yaml != null && json == null -> true
            yaml == null && json != null -> true
            else -> throw IllegalArgumentException("'yaml' or 'json' argument must be passed once.")
        }
    }


    private fun convertYamlToLua(yaml: String): String {
        val yamlObject = Yaml().load<Map<String, Any>>(yaml)
        return convertMapToLuaTable(yamlObject).replace(",}", "}")
    }

    private fun convertMapToLuaTable(map : Map<*, *>) : String {
        val luaTable = StringBuilder("{")
        for (entry in map.entries) {
            val key = entry.key
            val value = entry.value
            luaTable.append("[ [=[$key]=] ]=")
            luaTable.append(convertValueToLua(value))
            luaTable.append(",")
        }
        luaTable.append("}")
        return luaTable.toString()
    }

    private fun convertValueToLua(value : Any?) : String {
        return when (value) {
            is Map<*, *> -> convertMapToLuaTable(value)
            is List<*> -> convertListToLuaTable(value)
            is String -> "[=[$value]=]"
            else -> value.toString()
        }
    }

    private fun convertListToLuaTable(list : List<*>) : String {
        val luaTable = StringBuilder("{")
        list.forEachIndexed { index, value ->
            luaTable.append("[$index]=")
            luaTable.append(convertValueToLua(value))
            luaTable.append(",")
        }
        luaTable.append("}")
        return luaTable.toString()
    }

    private fun convertJsonToLua(json : String) : String {
        val jsonObject = JSONObject(json)
        val luaTable = StringBuilder("{")
        val keys = jsonObject.keys()

        while (keys.hasNext()) {
            val key = keys.next()
            val value = jsonObject.get(key)

            luaTable.append("[ [=[$key]=] ]=")

            when (value) {
                is JSONObject -> luaTable.append(convertJsonToLua(value.toString()))
                is JSONArray -> {
                    luaTable.append("{${value.joinToString(",") { makeParam(it) }}}")
                }
                is String -> luaTable.append("[=[$value]=]")
                is Int, Long, Float, Double -> luaTable.append(value)
                else -> throw IllegalArgumentException("Unsupported value type: ${value.javaClass}")
            }

            if (keys.hasNext()) {
                luaTable.append(",")
            }
        }

        luaTable.append("}")

        return luaTable.toString()
    }

    fun readAsLuaTable() : String? {
        return when {
            yaml != null && accessToTable -> convertYamlToLua(yaml)
            json != null && accessToTable -> convertJsonToLua(json)

            else -> null
        }
    }
}