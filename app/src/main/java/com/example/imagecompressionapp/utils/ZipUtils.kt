package com.example.imagecompressionapp.utils

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object ZipManager {

    private const val BUFFER_SIZE = 6 * 1024

    @Throws(IOException::class)
    fun zip(files: Array<String>, zipFile: String?) {
        var origin: BufferedInputStream? = null
        val out = ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile)))
        try {
            val data = ByteArray(BUFFER_SIZE)
            for (i in files.indices) {
                val fi = FileInputStream(files[i])
                origin = BufferedInputStream(fi, BUFFER_SIZE)
                try {
                    val entry = ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1))
                    out.putNextEntry(entry)
                    var count: Int
                    while (origin.read(data, 0, BUFFER_SIZE).also { count = it } != -1) {
                        out.write(data, 0, count)
                    }
                } finally {
                    origin.close()
                }
            }
        } finally {
            out.close()
        }
    }

    suspend fun <T> withZipFromUri(
        context: Context,
        uri: Uri, block: suspend (ZipInputStream) -> T
    ) : T =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                context.contentResolver.openInputStream(uri).use { input ->
                    if (input == null) throw FileNotFoundException("openInputStream failed")
                    ZipInputStream(input).use {
                        block.invoke(it)
                    }
                }
            }.getOrThrow()
        }
}