package com.example.catchai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catchai.GenerativeAi
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.TextPart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class FactCheckResult(
    val isReal: Boolean,
    val confidenceScore: Int,
    val detailedAnalysis: String,
    val trustedSources: List<String>
)

sealed class ChatMessage {
    data class UserMessage(val text: String) : ChatMessage()
    data class ModelFactCheck(val result: FactCheckResult) : ChatMessage()
    data class ModelError(val message: String) : ChatMessage()
}

class ChatViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<ChatUiState> =
        MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> =
        _uiState.asStateFlow()

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnMessageChange -> {
                _uiState.value = _uiState.value.copy(message = event.message)
            }
            is ChatEvent.OnSendMessage -> {
                sendMessage(event.message)
            }
        }
    }

    private fun sendMessage(message: String) {
        if (message.isBlank()) return

        val userMessage = ChatMessage.UserMessage(message)
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            message = "",
            chatHistory = _uiState.value.chatHistory + userMessage
        )

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    val prompt = "Fact-check the following claim. Respond with a JSON object containing these fields: \"isReal\" (boolean), \"confidenceScore\" (integer, 0-100), \"detailedAnalysis\" (string), and \"trustedSources\" (a list of string URLs). If the message is not a fact-checkable claim, respond with a standard greeting and an apology. Claim: $message"
                    GenerativeAi.model.generateContent(prompt)
                }

                val jsonResponse = response.text?.replace("```json", "")?.replace("```", "")?.trim()

                if (jsonResponse != null) {
                    try {
                        val factCheckResult = Json.decodeFromString<FactCheckResult>(jsonResponse)
                        val modelMessage = ChatMessage.ModelFactCheck(factCheckResult)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            chatHistory = _uiState.value.chatHistory + modelMessage
                        )
                    } catch (e: Exception) {
                        val modelMessage = ChatMessage.ModelError(response.text ?: "I can't seem to fact check that, can you try again?")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            chatHistory = _uiState.value.chatHistory + modelMessage
                        )
                    }
                } else {
                    val modelMessage = ChatMessage.ModelError("I can't seem to fact check that, can you try again?")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        chatHistory = _uiState.value.chatHistory + modelMessage
                    )
                }
            } catch (e: Exception) {
                val errorMessage = e.localizedMessage ?: "An error occurred"
                val modelMessage = ChatMessage.ModelError(errorMessage)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = errorMessage,
                    chatHistory = _uiState.value.chatHistory + modelMessage
                )
            }
        }
    }
}

data class ChatUiState(
    val message: String = "",
    val chatHistory: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class ChatEvent {
    data class OnMessageChange(val message: String) : ChatEvent()
    data class OnSendMessage(val message: String) : ChatEvent()
}