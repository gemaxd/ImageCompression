package com.example.imagecompressionapp.presentation.file_compression

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagecompressionapp.domain.file_compression.FileCompressionUIEvent
import com.example.imagecompressionapp.domain.file_compression.FileCompressionUIState
import com.example.imagecompressionapp.utils.CompressFileUtils.getFolderSizeLabel
import com.example.imagecompressionapp.utils.ZipManager
import kotlinx.coroutines.*
import java.io.File

class FileCompressionViewModel: ViewModel() {

    private var _file_compression_uiState = mutableStateOf(FileCompressionUIState())
    val fileCompressionUiState: State<FileCompressionUIState> = _file_compression_uiState

    private var _compressing = mutableStateOf(false)
    val compressing = _compressing

    fun onEvent(event: FileCompressionUIEvent){
        when(event){

            is FileCompressionUIEvent.CompressFile -> {
                setIsCompressing(true)
                _file_compression_uiState.value = _file_compression_uiState.value.copy(
                    sourceFileUri = event.sourceFile.toUri()
                )
                viewModelScope.launch(Dispatchers.IO) {
                    compressFileAsync(
                        sourceFile = event.sourceFile,
                        path = event.path,
                        pathToFile = event.pathToFile,
                        destinyFile = event.destinyFile
                    ).await()

                    _file_compression_uiState.value.destinyFile?.let {
                        onEvent(FileCompressionUIEvent.SetDestinyFile(it))
                    }
                    setIsCompressing()
                }
            }

            is FileCompressionUIEvent.ChangeLog -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _file_compression_uiState.value = _file_compression_uiState.value.copy(
                        compressionLog = event.compressionLog
                    )
                }
                viewModelScope.launch(Dispatchers.IO) {
                    onEvent(FileCompressionUIEvent.SetDestinyFile(event.destinyFile))
                }
            }

            is FileCompressionUIEvent.SetSourceFile -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _file_compression_uiState.value = _file_compression_uiState.value.copy(
                        sourceFile = event.sourceFile,
                        destinyFile = null,
                        compressionLog = ""
                    )
                    onEvent(
                        FileCompressionUIEvent.SetOriginalFileSpecs(
                            originalFileSpecs = mountOriginalFileSpecs(event.sourceFile)
                        )
                    )
                }
            }

            is FileCompressionUIEvent.SetDestinyFile -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _file_compression_uiState.value = _file_compression_uiState.value.copy(
                        destinyFile = event.destinyFile
                    )
                    onEvent(
                        FileCompressionUIEvent.SetCompressedFileSpecs(
                            compressedFileSpecs = mountCompressedFileSpecs(event.destinyFile)
                        )
                    )
                }
            }

            is FileCompressionUIEvent.SetOriginalFileSpecs -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _file_compression_uiState.value = _file_compression_uiState.value.copy(
                        originalFileSpecs = event.originalFileSpecs
                    )
                }
            }

            is FileCompressionUIEvent.SetCompressedFileSpecs -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _file_compression_uiState.value = _file_compression_uiState.value.copy(
                        compressedFileSpecs = event.compressedFileSpecs
                    )
                }
            }

        }
    }

    private fun setIsCompressing(status: Boolean = false){
        _compressing.value = status
    }

    private fun mountOriginalFileSpecs(originalFile: File): String{
        originalFile.let{
            return getFolderSizeLabel(originalFile) + " " +
                    "\nTipo do arquivo ${it.absolutePath.substring(it.absolutePath.lastIndexOf("."))}"
        }
    }

    private fun mountCompressedFileSpecs(destinationFile: File): String{
        destinationFile.let{
            return getFolderSizeLabel(destinationFile) + " " +
                    "\nTipo do arquivo ${it.absolutePath.substring(it.absolutePath.lastIndexOf("."))}"
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun compressFileAsync(sourceFile: File, path: String, pathToFile: String, destinyFile: File) = GlobalScope.async{
        ZipManager.zipTest(sourceFile, pathToFile)
        _file_compression_uiState.value = _file_compression_uiState.value.copy(
            destinyFile = destinyFile
        )
    }
}