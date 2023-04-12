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

package lua

object Description {
    private const val LIBRARY_NAME : String = "Kotlin/Lua"
    private const val LIBRARY_VERSION : String = "0.1-PREVIEW"
    private const val LIBRARY_LICENSE : String = "GNU Lesser GPL-2.1"
    private const val REPOSITORY_URL : String = "https://github.com/defaultzon3/KotlinLua"
    private const val RELEASE_DATE : String = "00.00.0000"

    val FILE_COMMENTARY : String = """
        --- This Lua script was created using the Kotlin language, the library $LIBRARY_NAME.
        --- The library is licensed under $LIBRARY_LICENSE; you can get the source code from the link below.
        ---
        ---     $REPOSITORY_URL
        ---
        --- Version library $LIBRARY_NAME from $RELEASE_DATE: $LIBRARY_VERSION
        --- Please do not remove this post. This will help the further development of library $LIBRARY_NAME.
        
    """.trimIndent()
}