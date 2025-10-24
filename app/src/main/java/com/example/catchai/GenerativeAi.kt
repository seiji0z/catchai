package com.example.catchai

import com.google.ai.client.generativeai.GenerativeModel

object GenerativeAi {
    val model = GenerativeModel(
        modelName = "models/gemini-2.5-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    val visionModel = GenerativeModel(
        modelName = "models/gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )
}