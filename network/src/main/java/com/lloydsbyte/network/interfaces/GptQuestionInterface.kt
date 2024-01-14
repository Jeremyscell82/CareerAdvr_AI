package com.lloydsbyte.network.interfaces

import com.lloydsbyte.network.responses.ChatGptResponse


interface GptQuestionInterface {
    fun onNetworkComplete(result: ChatGptResponse.GptData)
    fun onNetworkError(error: Throwable)
}