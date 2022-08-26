package com.example.imagecompressionapp.presentation

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.imagecompressionapp.MainViewModel
import com.example.imagecompressionapp.R
import com.example.imagecompressionapp.domain.UIEvent
import com.example.imagecompressionapp.utils.getDestinationFileForImage
import com.example.imagecompressionapp.utils.getFilePathFromUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ImageCompressorComponent(viewModel: MainViewModel = viewModel()) {

    val state = viewModel.uiState.value
    var compressedImageFile by remember { mutableStateOf<File?>(null) }
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {  uri: Uri ->
        uri.let{
            viewModel.onEvent(UIEvent.SetSourceFile(File(getFilePathFromUri(ctx, it, false))))
        }
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Antes",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )

                state.sourceFile?.let {
                    SubcomposeAsyncImage(
                        modifier = Modifier.padding(2.dp),
                        model = ImageRequest.Builder(ctx)
                            .data(it)
                            .crossfade(true)
                            .build(),
                        loading = { CircularProgressIndicator() },
                        contentDescription = null
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Depois",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )

                state.destinyFile?.let {
                    SubcomposeAsyncImage(
                        modifier = Modifier.padding(2.dp),
                        model = ImageRequest.Builder(ctx)
                            .data(it)
                            .build(),
                        loading = { CircularProgressIndicator() },
                        contentDescription = null
                    )
                }
            }
        }

        Text(
            text = state.compressionLog,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light,
            fontSize = MaterialTheme.typography.caption.fontSize
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {

            Button(
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f),
                onClick = {  launcher.launch("image/*") }
            ) {
                Text(text = "Pick Image")
            }

            Button(
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f),
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        compressedImageFile = ctx.getDestinationFileForImage()
                        compressedImageFile?.let {
                            viewModel.onEvent(UIEvent.CompressImage(state.sourceFile!!, it))
                        }
                    }
                }
            ){
                Text(text = "Compress Image")
            }
        }
    }
}