package com.example.imagecompressionapp.presentation.file_compression

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.imagecompressionapp.domain.file_compression.FileCompressionUIEvent
import com.example.imagecompressionapp.utils.getFilePathFromUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun FileCompressorComponent(viewModel: FileCompressionViewModel = viewModel()) {

    val scope = rememberCoroutineScope()
    val state = viewModel.fileCompressionUiState.value
    val ctx = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {  uri: Uri ->
        val originalFile = File(getFilePathFromUri(ctx, uri, false))
        viewModel.onEvent(
            FileCompressionUIEvent.SetSourceFile(
                originalFile
            )
        )
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Original file specs",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.caption.fontSize,
                    fontWeight = FontWeight.Light
                )

                state.originalFileSpecs?.let{
                    Text(
                        text = it,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = MaterialTheme.typography.caption.fontSize,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Compressed file specs",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.caption.fontSize,
                    fontWeight = FontWeight.Light
                )

                state.compressedFileSpecs?.let {
                    Text(
                        text = it,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = MaterialTheme.typography.caption.fontSize,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }

        Spacer(modifier = Modifier.padding(5.dp))

        Button(onClick = {
            launcher.launch("*/*")
        }) {
            Text(text = "File Pick")
        }

        Button(onClick = {
            state.sourceFile?.let {
                scope.launch(Dispatchers.IO) {
                    val zipName = "test.zip"
                    val path = ctx.filesDir.path
                    val pathToFile = "$path/$zipName"
                    val destinationFile = File(path, zipName)

                    viewModel.onEvent(
                        FileCompressionUIEvent.CompressFile(
                            sourceFile = it,
                            path = path,
                            pathToFile = pathToFile,
                            destinyFile = destinationFile
                        )
                    )
                }
            }
        }) {
            Text(text = "File Compress!")
        }
    }
}
