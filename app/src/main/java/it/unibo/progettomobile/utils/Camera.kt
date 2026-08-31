package it.unibo.progettomobile.utils

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun rememberCameraLauncher(
    onPictureTaken: (Uri) -> Unit = {}
): Pair<Uri?, () -> Unit> {
    var launcherUri by remember { mutableStateOf<Uri?>(null) }
    var pictureUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { pictureTaken ->
        if (pictureTaken) launcherUri?.let {
            pictureUri = it
            onPictureTaken(it)
        }
    }

    val ctx = LocalContext.current
    val takePicture = {
        try {
            val cacheDir = ctx.externalCacheDir ?: ctx.cacheDir
            val file = File.createTempFile("tmp_image_", ".jpg", cacheDir)
            val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.provider", file)
            launcherUri = uri
            launcher.launch(uri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    return pictureUri to takePicture
}
