package com.example.catchai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catchai.GenerativeAi
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.type.Content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<ChatUiState> =
        MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> =
        _uiState.asStateFlow()

    private val chat: Chat = GenerativeAi.model.startChat(history = emptyList())

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

        _uiState.value = _uiState.value.copy(isLoading = true, message = "")

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    chat.sendMessage(message)
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    chatHistory = chat.history
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage
                )
            }
        }
    }
}

data class ChatUiState(
    val message: String = "",
    val chatHistory: List<Content> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class ChatEvent {
    data class OnMessageChange(val message: String) : ChatEvent()
    data class OnSendMessage(val message: String) : ChatEvent()
}
