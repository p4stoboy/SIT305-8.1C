import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*


class ChatViewModel : ViewModel() {
    private val _chatState = mutableStateOf(ChatState(emptyList()))
    val chatState: State<ChatState> = _chatState

    fun sendMessage(username: String, messageText: String) {
        val userMessage = ChatMessage("user", messageText)
        _chatState.value = _chatState.value.copy(
            messages = _chatState.value.messages + userMessage
        )

        viewModelScope.launch {
            try {
                val response = localAPIReq(_chatState.value)
                val botMessage = ChatMessage("assistant", response)
                Log.d("ChatViewModel", "Bot response: $response")
                _chatState.value = _chatState.value.copy(
                    messages = _chatState.value.messages + botMessage
                )
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error", e)
            }
        }
    }

    private suspend fun localAPIReq(chatState: ChatState): String {
        val apiUrl = "http://10.0.2.2:7777/v1/chat/completions"

        val requestBody = buildJsonObject {
            put("messages", JsonArray(chatState.messages.map { message ->
                buildJsonObject {
                    put("role", message.role)
                    put("content", message.content)
                }
            }))
            put("temperature", 0.975)
            put("max_tokens", 400)
            put("stream", false)
        }.toString()

        return HttpClient().use { client ->
            val response: HttpResponse = client.post(apiUrl) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            val responseBody = response.bodyAsText()
            val jsonResponse = try {
                Json.decodeFromString<JsonObject>(responseBody)
            } catch (e: Exception) {
                Log.d("ChatViewModel", "Response body: $responseBody")
                Log.e("ChatViewModel", "Error parsing JSON", e)
                throw e
            }
            val choices = jsonResponse["choices"] as? JsonArray
            val firstChoice = choices?.firstOrNull() as? JsonObject
            val message = firstChoice?.get("message") as? JsonObject
            // this avoids terrible string formatting than can result from using toString() on the json object
            val content = message?.get("content")?.jsonPrimitive?.content

            return content ?: throw Exception("Content field is missing or invalid")
        }
    }
}
