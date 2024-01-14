package com.lloydsbyte.network.interfaces

import com.lloydsbyte.network.ConfigModel

interface ConfigFileInterface {
    fun onComplete(file: ConfigModel.BaseStructure)
    fun onFailed(error: Throwable)
}