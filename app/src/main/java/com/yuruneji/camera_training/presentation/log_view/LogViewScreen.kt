package com.yuruneji.camera_training.presentation.log_view

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.yuruneji.camera_training.data.local.db.convert
import com.yuruneji.camera_training.presentation.log_view.state.LogViewState
import com.yuruneji.camera_training.presentation.ui.theme.CameraTrainingTheme

/**
 * @author toru
 * @version 1.0
 */
@Composable
fun LogViewScreen(
    navController: NavController,
    viewModel: LogViewViewModel = hiltViewModel()
) {
    CameraTrainingTheme {
        // val articles by viewModel.articlesStateFlow.collectAsState(initial = emptyList())
        val articles2 = viewModel.selectData().observeAsState()
        var logViewState: LogViewState = LogViewState()

        LaunchedEffect(key1 = Unit) {
            viewModel.setDate()
        }

        viewModel.logViewState.observe(LocalLifecycleOwner.current) {
            logViewState = it
            viewModel.setSelectCond(logViewState)
        }

        LazyColumn {
            items(
                items = articles2.value?.map { it.convert() } ?: emptyList(),
                key = { article -> article.uid }
            ) { article ->
                LogViewRow(
                    logViewItem = article,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun LogViewRow(logViewItem: LogViewItem, modifier: Modifier = Modifier) {
    val priorityStr = when (logViewItem.priority) {
        Log.VERBOSE -> "VERBOSE"
        Log.DEBUG -> "DEBUG"
        Log.INFO -> "INFO"
        Log.WARN -> "WARN"
        Log.ERROR -> "ERROR"
        Log.ASSERT -> "ASSERT"
        else -> "UNKNOWN"
    }

    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = logViewItem.date,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = priorityStr,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = logViewItem.tag ?: "",
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = logViewItem.message,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview
@Composable
fun LogViewRowPreview() {
    CameraTrainingTheme {
        Surface {
            // LogViewRow(
            //     logViewItem = LogViewItem(
            //         uid = 1,
            //         date = "2023-07-21 10:10:10",
            //         priority = 1,
            //         tag = "Tag",
            //         message = "Message"
            //     ),
            //     modifier = Modifier.fillMaxWidth()
            // )
        }
    }
}
