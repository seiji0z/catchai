package com.example.catchai.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catchai.GenerativeAi
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class DetectionResult(
    val isAiGenerated: Boolean,
    val confidencePercentage: Int,
    val briefDescription: String,
    val originalSource: String? = null
)

class MediaDetectorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MediaDetectorUiState())
    val uiState: StateFlow<MediaDetectorUiState> = _uiState.asStateFlow()

    fun onEvent(event: MediaDetectorEvent) {
        when (event) {
            is MediaDetectorEvent.OnAnalyzeClick -> {
                analyzeImage(event.bitmap)
            }
        }
    }

    private fun analyzeImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = withContext(Dispatchers.IO) {
                    val inputContent = content {
                        image(bitmap)
                        text("Analyze the provided image for any signs of AI-generation. Respond with a JSON object containing these fields: \"isAiGenerated\" (boolean), \"confidencePercentage\" (integer), \"briefDescription\" (string), and optionally \"originalSource\" (string).")
                    }
                    GenerativeAi.visionModel.generateContent(inputContent)
                }

                val json = response.text?.replace("```json", "")?.replace("```", "")
                if (json != null) {
                    val detectionResult = Json.decodeFromString<DetectionResult>(json)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        detectionResult = detectionResult
                    )
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage
                )
            }
        }
    }
}

data class MediaDetectorUiState(
    val isLoading: Boolean = false,
    val detectionResult: DetectionResult? = null,
    val errorMessage: String? = null
)

sealed class MediaDetectorEvent {
    data class OnAnalyzeClick(val bitmap: Bitmap) : MediaDetectorEvent()
}