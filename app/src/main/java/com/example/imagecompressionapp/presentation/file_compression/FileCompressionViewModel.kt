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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

class FileCompressionViewModel: ViewModel() {

    private var _file_compression_uiState = mutableStateOf(FileCompressionUIState())
    val fileCompressionUiState: State<FileCompressionUIState> = _file_compression_uiState

    fun onEvent(event: FileCompressionUIEvent){
        when(event){

            is FileCompressionUIEvent.CompressFile -> {
                _file_compression_uiState.value = _file_compression_uiState.value.copy(
                    sourceFileUri = event.sourceFile.toUri()
                )
                viewModelScope.launch(Dispatchers.IO) {
                    compressFileAsync(
                        event.sourceFile,
                        event.path,
                        event.pathToFile,
                        event.destinyFile
                    ).await()

                    _file_compression_uiState.value.destinyFile?.let {
                        onEvent(FileCompressionUIEvent.SetDestinyFile(it))
                    }
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

    private suspend fun mountOriginalFileSpecs(originalFile: File): String{
        originalFile.let{
            return getFolderSizeLabel(originalFile) + " " +
                    "\nTipo do arquivo ${it.absolutePath.substring(it.absolutePath.lastIndexOf("."))}"
        }
    }

    private suspend fun mountCompressedFileSpecs(destinationFile: File): String{
        destinationFile.let{
            return getFolderSizeLabel(destinationFile) + " " +
                    "\nTipo do arquivo ${it.absolutePath.substring(it.absolutePath.lastIndexOf("."))}"
        }
    }

    private suspend fun compressFileAsync(sourceFile: File, path: String, pathToFile: String, destinyFile: File) = GlobalScope.async{

        ZipManager.zipTest(sourceFile, pathToFile)

        _file_compression_uiState.value = _file_compression_uiState.value.copy(
            destinyFile = destinyFile
        )
    }
}