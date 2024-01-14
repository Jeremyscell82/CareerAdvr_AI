package com.lloydsbyte.network.interfaces

import com.lloydsbyte.network.responses.DallEResponse

interface DallEInterface {
    fun onCompleted(data: DallEResponse.DallEData)
    fun onFailed(error: Throwable)
}