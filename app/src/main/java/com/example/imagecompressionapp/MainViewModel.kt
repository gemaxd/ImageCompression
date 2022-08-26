package com.example.imagecompressionapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagecompressionapp.domain.UIEvent
import com.example.imagecompressionapp.domain.UIState
import com.example.imagecompressionapp.utils.CompressFileUtils
import com.example.imagecompressionapp.utils.CompressUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel: ViewModel() {

    private var _uiState = mutableStateOf(UIState())
    val uiState: State<UIState> = _uiState

    fun onEvent(event: UIEvent){
        when(event){
            is UIEvent.CompressImage -> {
                _uiState.value = _uiState.value.copy(
                    sourceFileUri = event.sourceFile.toUri(),
                    destinyFileUri = event.destinationFile.toUri()
                )
                viewModelScope.launch(Dispatchers.IO) {
                    compressImage(event.sourceFile, event.destinationFile)
                }
            }
            is UIEvent.ChangeLog -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiState.value = _uiState.value.copy(
                        compressionLog = event.compressionLog
                    )
                }
                viewModelScope.launch(Dispatchers.IO) {
                    onEvent(UIEvent.SetDestinyFile(event.destinyFile))
                }
            }
            is UIEvent.SetSourceFile -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiState.value = _uiState.value.copy(
                        sourceFile = event.sourceFile,
                        destinyFile = null,
                        compressionLog = ""
                    )
                }
            }

            is UIEvent.SetDestinyFile -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiState.value = _uiState.value.copy(
                        destinyFile = event.destinyFile
                    )
                }
            }
        }
    }

    private val _timeSpend = mutableStateOf(0L)
    private val timeSpend = _timeSpend

    private suspend fun compressImage(sourceFile: File, destinationFile: File){
        viewModelScope.launch(Dispatchers.IO) {
            _timeSpend.value = CompressUtil.compressImage(sourceFile = sourceFile, destinationFile = destinationFile)

            _uiState.value.let{
                val log = StringBuilder()
                    .append("\nCaminho original: ${it.sourceFileUri?.path}")
                    .append("\nTamanho antes da compressão: ${CompressFileUtils.getFolderSizeLabel(sourceFile)}")
                    .append("\n-----------")
                    .append("\nQualidade Aplicada : ${CompressFileUtils.COMPRESS_QUALITY} %")
                    .append("\n-----------")
                    .append("\nCaminho da compressão : ${it.destinyFileUri?.path}")
                    .append("\nTamanho depois da compressão : ${CompressFileUtils.getFolderSizeLabel(destinationFile)}")
                    .append("\n-----------")
                    .append("\nTempo gasto : ${timeSpend.value} Ms")
                    .toString()

                onEvent(UIEvent.ChangeLog(log, destinationFile))
            }
        }
    }
}