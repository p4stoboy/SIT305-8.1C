import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    var username by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome, Let's Chat!")
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        Button(onClick = { onLoginSuccess(username) }) {
            Text(text = "Go")
        }
    }
}

// ChatScreen.kt
@Composable
fun ChatScreen(username: String, vm: ChatViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.Bottom, // Align items to the bottom
            reverseLayout = true // Reverse the order of items
        ) {
            items(vm.chatState.value.messages.reversed()) { message -> // Reverse the list of messages
                MessageBubble(message)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            var messageText by remember { mutableStateOf("") }
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            Button(onClick = {
                vm.sendMessage(username, messageText)
                messageText = ""
            }) {
                Text(text = "Send")
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val backgroundColor = if (message.role == "user") Color.LightGray else Color.White
    val alignment = if (message.role == "user") Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(16.dp),
        contentAlignment = alignment
    ) {
        Text(text = message.content)
    }
}
