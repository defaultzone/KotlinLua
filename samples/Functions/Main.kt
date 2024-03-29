import luaCore.*
import luaCore.funcOperations.* // For functions/arguments.

fun main() {

    /**
     * Here we register a function with an argument whose normal value is a string.
     * The function code itself acts as `(List<Argument>) -> Unit`, nothing needs to be returned.
     * Function arguments can be accessed using the `it` keyword.
     */

    val someFunction = LuaFunc(Argument("https://google.com")) {
        LuaNode("print(${it[0].readValue()})")
    }

    /**
     * To execute a function use `LuaFunc().execute()`.
     * The argument(s) is {String|Long|Int|Boolean|Float|Double|Table|null}
     * There can be as many arguments as you specified in `LuaFunc(/* your arguments */)`.
     */

    LuaNode(someFunction.execute("There can be local variable. ( use `LocalVar().readValue()` )"))
}

/**
 * Output:
 *
 * function _0(_1)
 * local _1 = _1 or [=[https://google.com]=]
 * print(_1)
 * end
 * _0([=[There can be local variable. ( use `LocalVar().readValue()` )]=])
 */
