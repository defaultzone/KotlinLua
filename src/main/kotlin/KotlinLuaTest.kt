import luaCore.*

fun main() {
    val firstVar = LocalVar("firstVar", "Hello, world!")
    var secondVar = LocalVar("secondVar", false)

    LuaNode("print(${firstVar.read()})")
    secondVar.change(true)
    LuaNode("print(${secondVar.read()})")

    println(Data.fileContent.joinToString("\n"))
}