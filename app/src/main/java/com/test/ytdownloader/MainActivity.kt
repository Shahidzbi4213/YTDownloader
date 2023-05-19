package com.test.ytdownloader

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.test.ytdownloader.ui.theme.YTDownloaderTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TAG = "YTS"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YTDownloaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    MainScreen()

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    var text by remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text, onValueChange = { text = it },
            placeholder = { Text(text = "Type youtube Url Here") },
            trailingIcon = {
                if (text.isNotEmpty())
                    Icon(imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            text = ""
                            focusManager.clearFocus()
                        })
            },
            modifier = Modifier.fillMaxWidth(0.95f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            scope.launch(context = Dispatchers.IO) {
                 downloadVideo(
                     text.ifEmpty { "https://youtu.be/7ukzzjtUCJA" },
                     context
                 )


            }
        }) {
            /*Text(text = "Download Now")*/
        }

    }
}

 fun downloadVideo(url: String, context: Context) {


    object : YouTubeExtractor(context) {
        override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {

            Log.d(TAG, "onExtractionComplete: $ytFiles")
            Log.d(TAG, "onExtractionComplete: $videoMeta")

            if (ytFiles != null) {

                val downloadUrl = ytFiles[22].url

                Log.d(TAG, "onExtractionComplete: $downloadUrl")
            }

        }


    }.extract(url, false, false)

}
