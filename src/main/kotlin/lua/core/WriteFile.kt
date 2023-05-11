/**
 *  Free and Open Source library for building Lua scripts, using Kotlin. (KotlinLua)
 *  Copyright (C) 2023 defaultzon3
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *  USA
 */

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
 * @param fileContent {() -> Unit}
 */
fun writeFile(
    path : String,
    charset : String = "UTF-8",
    descriptor : Descriptor = Descriptor(),
    fileContent : (descriptor : Descriptor) -> Unit
) {
    Data.fileContent = emptyArray() // Delete all contents of the file up to the "invoke" lambda.
    fileContent(descriptor)
    File(path).writeText(Description.FILE_COMMENTARY + "\n${Data.fileHeader}\n" + Data.fileContent.joinToString("\n") {
        // If "it" != NULL_LUA_NODE we're writing line in file, otherwise we add "".
        it.takeIf { it != "NULL_LUA_NODE" } ?: ""
    }, charset(charset))
    Data.fileContent    = emptyArray() // Delete all contents of the file after writing file.
    Data.fileHeader     = ""
}