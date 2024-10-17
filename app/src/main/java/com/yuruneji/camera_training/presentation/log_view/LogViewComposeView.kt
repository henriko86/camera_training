package com.yuruneji.camera_training.presentation.log_view

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.AbstractComposeView
import com.yuruneji.camera_training.presentation.ui.theme.CameraTrainingTheme

/**
 * @author toru
 * @version 1.0
 */
class LogViewComposeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AbstractComposeView(context, attrs, defStyle) {

    // Compose の State をフィールドとして持つ
    var logViewItem by mutableStateOf<LogViewItem?>(null)

    @Composable
    override fun Content() {
        CameraTrainingTheme {
            // Compose の State をコンポーズ可能な関数内で読む
            val logViewItem = logViewItem

            if (logViewItem != null) {
                // LogViewRow(
                //     logViewItem = logViewItem,
                //     modifier = Modifier.fillMaxWidth()
                // )
            }
        }
    }
}
