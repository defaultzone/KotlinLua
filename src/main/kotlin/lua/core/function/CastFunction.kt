package lua.core.function

import lua.node.LuaNode

class CastFunction(private val luaFunction : String, private val returnClass : Return) {
    fun toLuaNode() : LuaNode = LuaNode(luaFunction)

    fun getReturnClass() : Return = returnClass

    override fun toString() : String = luaFunction
}