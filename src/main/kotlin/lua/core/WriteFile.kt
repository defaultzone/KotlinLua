package lua.core

import lua.Description
import lua.node.Data
import java.io.File

/**
 * Write a file by concatenating lines from `Data.fileContent`.
 * Note that only what is done in the "invoke" lambda will be written to the file.
 *
 * @param path {String}
 * @param charset {String}: Default - UTF-8.
 * @param invoke {() -> Unit}
 */
fun writeFile(path : String, charset : String = "UTF-8", invoke : () -> Unit) {
    Data.fileContent = emptyArray() // Delete all contents of the file up to the "invoke" lambda.
    invoke()
    File(path).writeText(Description.FILE_COMMENTARY + "\n" + Data.fileContent.joinToString("\n") {
        // If "it" != NULL_LUA_NODE we're writing line in file, otherwise we add "".
        it.takeIf { it != "NULL_LUA_NODE" } ?: ""
    }, charset(charset))
    Data.fileContent = emptyArray() // Delete all contents of the file after writing file.
}