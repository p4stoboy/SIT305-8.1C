data class ChatMessage(val role: String, val content: String)

data class ChatState(val messages: List<ChatMessage>)

