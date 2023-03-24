import luaCore.*

/**
 * Registering local variables sample.
 *
 * Usage: `val someVar = LocalVar(varName, value)`, where:
 *      varName {String}: Name for variable, should match the following pattern: `[A-Za-z_][A-Za-z0-9_]+`
 *      value {String|Int|Long|Boolean|Nothing(null)}: Value for variable, by default it's `NullPointerException` or `nil`.
 * Functions:
 *      fun read() : String;
 *      fun change(value: Any) : LuaNode
 * In the example below, the result would be:
 *
 * local firstVar = [=[Hello, world!]=]
 * local secondVar = false
 * print(firstVar)
 * secondVar = true
 * print(secondVar)
 */

fun main() {
    val firstVar = LocalVar("firstVar", "Hello, world!")
    val secondVar = LocalVar("secondVar", false)

    LuaNode("print(${firstVar.read()})")
    secondVar.change(true)
    LuaNode("print(${secondVar.read()})")
}