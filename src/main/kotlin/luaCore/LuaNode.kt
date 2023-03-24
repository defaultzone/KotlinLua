package luaCore

class LuaNode(private val content : String) {
    init {
        if (content != "NULL_LUA_NODE") Data.fileContent += arrayOf(content)
    }

    override fun toString() : String = content
}