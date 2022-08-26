package com.example.imagecompressionapp.presentation

import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
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
import com.example.imagecompressionapp.utils.CompressFileUtils
import com.example.imagecompressionapp.utils.ZipManager.zip
import com.example.imagecompressionapp.utils.getFilePathFromUri
import java.io.File
import java.util.zip.ZipFile

@Composable
fun FileCompressorComponent() {
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var compressedFileUri by remember { mutableStateOf<Uri?>(null) }

    var originalFile by remember { mutableStateOf<File?>(null) }
    var compressedFile by remember { mutableStateOf<ZipFile?>(null) }

    var originalFileSpecs by remember { mutableStateOf<String?>(null) }
    var compressedFileSpecs by remember { mutableStateOf<String?>(null) }

    var logMessages by remember { mutableStateOf("") }

    val ctx = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {  uri: Uri ->
        fileUri = uri
        originalFile = File(getFilePathFromUri(ctx, uri, false))
        originalFile?.let {
            originalFileSpecs = CompressFileUtils.getFolderSizeLabel(it) + " " +
                    "\nTipo do arquivo ${it.absolutePath.substring(it.absolutePath.lastIndexOf("."))}"
        }
    }

    compressedFile?.let {
        compressedFileSpecs = "${it.size()} \nArquivo .ZIP"
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

                originalFileSpecs?.let{
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

                compressedFileSpecs?.let {
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
            originalFile?.let {
                zip(arrayOf(it.path), it.name)
                compressedFile = ZipFile(it)
            }
        }) {
            Text(text = "File Compress!")
        }
    }
}
