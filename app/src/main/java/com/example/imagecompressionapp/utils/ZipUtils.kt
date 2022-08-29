package com.example.imagecompressionapp.utils

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object ZipManager {
    fun zipTest(sourceFile: File, destinationPath: String) {
        val files: Array<String> = arrayOf(sourceFile.path)
        val out = ZipOutputStream(BufferedOutputStream(FileOutputStream(destinationPath)))
        for (file in files) {
            val fi = FileInputStream(file)
            val origin = BufferedInputStream(fi)
            val entry = ZipEntry(file.substring(file.lastIndexOf("/")))
            out.putNextEntry(entry)
            origin.copyTo(out, 1024)
            origin.close()
        }
        out.close()
    }
}