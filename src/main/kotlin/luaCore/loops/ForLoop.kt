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

package luaCore.loops

import luaCore.Data
import luaCore.LocalVar
import luaCore.LuaNode

object ForLoop {

    /**
     * Get variable name as `_{{ bit value of Data.currentItemNode}}`.
     * When used, Data.currentItemNode will be incremented.
     * @return {String}: Name of variable.
     */

    private fun getVarName() : String {
        val name : String = "_" + Data.currentItemNode.toString(2)
        Data.currentItemNode++
        return name
    }

    /**
     * Get the name of the variable assigned to the table via `LocalVar`.
     *
     * @param table {LocalVar}
     * @return {Pair<Boolean, String>}
     */

    private fun getTableName(table : LocalVar) : Pair<Boolean, String> {
        return when (table.type()) {
            "Table" -> Pair(true, table.read()!!)
            else    -> {
                println("""
                    [ warning ]: Unknown type of variable in argument (ForLoop) 'table'.
                    [ info ]: Type is '${table.type()}', but should be 'Table'.
                """.trimIndent())
                Pair(false, "NULL")
            }
        }
    }

    /**
     * Registration of a "for" loop with start, end and step (default - 1).
     * The last argument takes `Unit`, passing in the name of the variable that is set to start.
     * When used, it will add the following to the contents of the file:
     *
     * ```lua
     * for ${{ varName }} = int_start, int_end, int_step do
     *     execute_loop_content()
     * end
     * ```
     *
     * @param start {Int}
     * @param end {Int}
     * @param loopContent {(index : String) -> Unit}
     */

    fun default(start : Int, end : Int, step : Int = 1, loopContent : (index : String) -> Unit) {
        val varName : String = getVarName()
        LuaNode("for $varName = $start, $end, $step do")
        loopContent(varName)
        LuaNode("end")
    }

    /**
     * On usage will add in content of the file next:
     * The last argument takes `Unit`, passing names of key and values.
     * ```lua
     * for ${{ keyName }}, ${{ valueName }} in pairs(LocalVar_table) do
     *     execute_loop_content()
     * end
     * ```
     * @param table {LocalVar}
     * @param loopContent {(key : String, value : String) -> Unit}
     */

    fun pairs(table : LocalVar, loopContent : (key : String, value : String) -> Unit) {
        val keyName : String = getVarName()
        val valueName : String = getVarName()
        val (accessToTable, tableName) = getTableName(table)

        if (accessToTable) {
            LuaNode("for $keyName, $valueName in pairs($tableName) do")
            loopContent(keyName, valueName)
            LuaNode("end")
        }
    }

    /**
     * On usage will add in content of the file next:
     * The last argument takes `Unit`, passing names of key and values.
     * ```lua
     * for ${{ indexName }}, ${{ valueName }} in ipairs(LocalVar_table) do
     *     execute_loop_content()
     * end
     * ```
     * @param table {LocalVar}
     * @param loopContent {(key : String, value : String) -> Unit}
     */

    fun ipairs(table : LocalVar, loopContent : (indexName : String, value : String) -> Unit) {
        val indexName : String = getVarName()
        val valueName : String = getVarName()
        val (accessToTable, tableName) = getTableName(table)

        if (accessToTable) {
            LuaNode("for $indexName, $valueName in ipairs($tableName) do")
            loopContent(indexName, valueName)
            LuaNode("end")
        }
    }
}