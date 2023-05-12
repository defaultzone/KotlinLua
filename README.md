### Kotlin/Lua v0.1-PREVIEW

**Free and Open Source** library for building Lua scripts using Kotlin.<br>
Licensed under terms of **GNU Lesser General Public License v2.1:** [**learn more**](https://github.com/defaultzon3/KotlinLua/blob/main/LICENSE)
***
### Library connection

```groovy
// build.gradle.kts
// ...
dependencies {
    implementation(files("path/to/library.jar")) // Kotlin syntax
    implementation files('path/to/library.jar')  // Groovy syntax
}
```
***

### Usage example
> To reduce the `String`s (and file weight respectively) in your code, we use naming something like `_$Data.currentItemNode`.<br>
> The maximum size of `Data.currentItemNode` is `Long`.

#### Kotlin

```kotlin
import lua.core.*
import lua.core.function.Function
import lua.core.function.Argument
import lua.core.loop.ForLoop

fun main() {
    writeFile("/some/path/file.lua", descriptor = Descriptor {
        arrayOf( DescriptorObject("io.write", 0) )
    }) { descriptor ->
        // Generic in class `Function` is what the lambda will return.
        // Use class `Return` in generic, if you want the function to return something.
        val fizzBuzz : Function<Unit> = Function(Argument()) {
            Cond.If("${it[0].read()} % 3 == 0", useEndKeyword = false) {
                Cond.If("${it[0].read()} % 5 == 0") {
                    /*
                     * To execute function from descriptor, we need to use next syntax:
                     * `descriptor["name"](...params: Any?).toLuaNode()`
                     * Since `descriptor["name"](...params: Any?)` returns String, we need to convert this string to LuaNode,
                     * and then this descriptor will be assembled in file, that passed by `path` argument in `writeFile` function.
                     */
                    descriptor["io.write"]("FizzBuzz").toLuaNode()
                    Cond.Else {
                        descriptor["io.write"]("Fizz").toLuaNode()
                    }
                }; Cond.ElseIf("${it[0].read()} % 5 == 0", useEndKeyword = true) {
                descriptor["io.write"]("Buzz").toLuaNode()
                Cond.Else {
                    descriptor["io.write"](it[0]).toLuaNode()
                }
            }
            }
            descriptor["io.write"]("\\n").toLuaNode()
        }

        ForLoop.default(1, 100) { index ->
            fizzBuzz.execute(index).toLuaNode()
        }
    }
}
```
### Lua

> The library **does not format the code** resulting from the concatenation of all `LuaNode`s,<br>
> since to read it with names like `_$int` already makes a problem (why - see above), so we **abandoned the formatting**. 

```lua
local _0={_0=io.write}
function _2(_1)
if _1 % 3 == 0 then
if _1 % 5 == 0 then
--[[-1]]_0._0([=[FizzBuzz]=])
else
--[[-1]]_0._0([=[Fizz]=])
end
elseif _1 % 5 == 0 then
--[[-1]]_0._0([=[Buzz]=])
else
--[[-1]]_0._0(_1)
end
--[[-1]]_0._0([=[\n]=])
end
for _3 = 1, 100, 1 do
--[[-1]]_2(_3)
end
```

This example does not show the entire structure of the library (variables, functions, classes, etc.), more examples can be found at the links below:
- [**`samples` directory**](https://github.com/defaultzone/KotlinLua/tree/main/samples)
- [**Kotlin/Lua wiki**](https://github.com/defaultzone/KotlinLua/wiki)

***
**This library is still under development, assistance is welcome.**<br>
Kotlin/Lua Copyright (C) 2023 defaultzone
