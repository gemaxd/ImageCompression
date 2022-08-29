package com.example.imagecompressionapp.domain.image_compression

import android.net.Uri
import java.io.File

data class ImageCompressionUIState(
    val sourceFile: File? = null,
    val destinyFile: File? = null,
    val sourceFileUri: Uri? = null,
    val destinyFileUri: Uri? = null,
    val compressionLog: String = ""
)
