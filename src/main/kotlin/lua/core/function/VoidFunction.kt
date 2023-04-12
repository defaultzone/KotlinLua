package lua.core.function

import lua.core.makeParam
import lua.node.Data
import lua.node.LuaNode

class VoidFunction(vararg arguments : Argument, function : (List<Argument>) -> Unit) {
    private val varName = Data.currentItemNode.toString(2)

    init {
        Data.currentItemNode++
        LuaNode("function _$varName(${
            arguments.joinToString(",") { it.read() }
        })")
        arguments.forEach { it.insertNodeWithValue() }
        function(arguments.toList())
        LuaNode("end")
    }

    fun execute(vararg arguments : Any?) : String = "_$varName(${arguments.joinToString { makeParam(it) }})"
}