# Kotlin/Lua 0.1-PREVIEW
**Free and Open Source** library for building Lua scripts using Kotlin. Licensed under terms of **GNU Lesser General Public License v2.1**.
[**Learn more.**](https://github.com/defaultzon3/KotlinLua/blob/main/LICENSE)
***
**To use** this library, add in your `build.gradle.kts` following:
```groovy
dependencies {
    implementation(files("path/to/library.jar")) // Kotlin syntax
    implementation files('path/to/library.jar')  // Groovy syntax
}
```
**This will allow you to use the library in your project, and to include it in a file, use the following:**

```java
import lua.*
```
***
**Implemented**
- **Local variables:** [**see sample.**](https://github.com/defaultzon3/KotlinLua/blob/main/samples/LocalVariables/Main.kt)
- **Lua Functions, arguments:** [**see sample.**](https://github.com/defaultzon3/KotlinLua/blob/main/samples/Functions/Main.kt)
- **For, while loops**: _sample in progress_
- **Lua table**: [**see sample.**](https://github.com/defaultzon3/KotlinLua/blob/main/samples/Table/Main.kt)

**In progress**
- **If, else, elseif** statement.
- **Return** keyword in functions.
*** 
**This library is still under development, assistance is welcome.**
_**KotlinLua Copyright (C) 2023 defaultzon3**_
