import luaCore.*

/**
 * Registering local variables sample.
 *
 * Usage: `val someVar = LocalVar(value)`, where:
 *      value {String|Long|Int|Boolean|Float|Double|Table|null}: Value for variable, by default it's `NullPointerException` or `nil`.
 * Variable name acts as unsigned 16 bit (limit: 0-65535).
 *
 * Functions:
 *      fun read() : String;
 *      fun change(value: Any) : LuaNode
 * In the example below, the result would be:
 *
 * local _0 = [=[Hello, world!]=]
 * local _1 = false
 * print(_0)
 * _1 = true
 * print(_1)
 */

fun main() {
    val firstVar = LocalVar("Hello, world!")
    val secondVar = LocalVar(false)

    LuaNode("print(${firstVar.read()})")
    secondVar.change(true)
    LuaNode("print(${secondVar.read()})")
}