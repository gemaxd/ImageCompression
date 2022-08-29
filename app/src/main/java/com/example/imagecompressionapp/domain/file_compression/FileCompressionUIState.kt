package com.example.imagecompressionapp.domain.file_compression

import android.net.Uri
import java.io.File

data class FileCompressionUIState(
    val sourceFile: File? = null,
    val destinyFile: File? = null,
    val sourceFileUri: Uri? = null,
    val destinyFileUri: Uri? = null,
    val destinationFileName: String? = null,
    val path: String? = null,
    val pathToFile: String? = null,
    val compressionLog: String = "",
    val originalFileSpecs: String? = null,
    val compressedFileSpecs: String? = null
)
