import luaCore.*

/**
 * Пример с таблицами в KotlinLua. Устройство класса следующее:
 *
 * `class Table(private val yaml : String? = null, private val json : String? = null)`
 *
 */

fun main() {
    // YAML as argument:
    val yamlTable = LocalVar(Table("""
        name: John Wick
        age: 32
    """.trimIndent()))

    // JSON as argument.
    val jsonTable = LocalVar(Table(json = """
        {
            "name": "John Wick",
            "age": 32
        }
    """.trimIndent()))

    // Note that JSON or YAML is only passed as an argument once.
    // A call with two (or if there are none) arguments will issue a warning, the value of the variable will be replaced with `nil`.
    // To change value inside table, use next:

    yamlTable.tableChange("name", "John Smith")

    // Same with JSON:

    jsonTable.tableChange("name", "John Smith")

    /**
     * Result:
     *
     * local _0 = {["name"]=[=[John Wick]=],["age"]=32,}
     * local _1 = {["name"]=[=[John Wick]=], ["age"]=32}
     * _0["name"]=[=[John Smith]=]
     * _1["name"]=[=[John Smith]=]
     */
}