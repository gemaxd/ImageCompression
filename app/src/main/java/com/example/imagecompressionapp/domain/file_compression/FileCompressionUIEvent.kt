package com.example.imagecompressionapp.domain.file_compression

import java.io.File

sealed class FileCompressionUIEvent {
    data class SetDestinyFile(val destinyFile: File): FileCompressionUIEvent()
    data class SetSourceFile(val sourceFile: File): FileCompressionUIEvent()
    data class SetOriginalFileSpecs(val originalFileSpecs: String): FileCompressionUIEvent()
    data class SetCompressedFileSpecs(val compressedFileSpecs: String): FileCompressionUIEvent()
    data class CompressFile(val sourceFile: File, val path: String, val pathToFile: String, val destinyFile: File): FileCompressionUIEvent()
    data class ChangeLog(val compressionLog: String, val destinyFile: File): FileCompressionUIEvent()
}
