package SIT305.a8_1c

import ChatApp
import ChatViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import SIT305.a8_1c.ui.theme._81CTheme

// New things learned in this task:
// - XML network permission configuration
// - Ktor client library
// - Kotlin serialization library


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm = ChatViewModel()
        setContent {
            _81CTheme {
                ChatApp(vm)
            }
        }
    }
}
