package com.evervault.sdk.test

import java.nio.file.Files

fun writeFile(data: ByteArray) {
    val tempFile = Files.createTempFile("tempFile", ".tmp")
    Files.write(tempFile, data)
    println("Data written to temporary file: ${tempFile.toAbsolutePath()}")
}
