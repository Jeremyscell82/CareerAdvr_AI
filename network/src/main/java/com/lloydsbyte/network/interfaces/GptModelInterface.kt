package com.lloydsbyte.network.interfaces

import com.lloydsbyte.network.responses.ModelResponse

interface GptModelInterface {
    fun onComplete(result: ModelResponse.ModelData)
    fun onError(error: Throwable)
}