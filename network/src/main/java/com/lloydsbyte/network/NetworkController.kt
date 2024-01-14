package com.lloydsbyte.network

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lloydsbyte.network.examples.CountryInterface
import com.lloydsbyte.network.interfaces.ConfigFileInterface
import com.lloydsbyte.network.interfaces.DallEInterface
import com.lloydsbyte.network.interfaces.GptModelInterface
import com.lloydsbyte.network.interfaces.GptQuestionInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class NetworkController(
    private val networkClient: NetworkClient
) {

    private var disposable: Disposable? = null

    fun pullConfigFile(configInterface: ConfigFileInterface){
        disposable = networkClient.getConfigFile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { file ->
                    configInterface.onComplete(file)
                    disposable?.dispose()
                },
                { error ->
                    configInterface.onFailed(error)
                    disposable?.dispose()
                }
            )
    }

    fun pullCountriesApi(countriesApi: CountryInterface) {
        disposable = networkClient.getCountyList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    countriesApi.onPullCompleted(response)
                    disposable?.dispose()
                },
                { error ->
                    countriesApi.onPullFailed(error)
                    disposable?.dispose()
                }
            )
    }

    //Chat GPT
    fun pullAiModels(callback: GptModelInterface) {
        disposable = networkClient.getModels(NetworkConstants.ai_key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    callback.onComplete(result)
                    destroyConnection()
                },
                { error ->
                    when {
                        retryCount < retryMax -> {
                            retryCount += 1
                            pullAiModels(callback)
                        }
                        else -> {
                            callback.onError(error)
                            destroyConnection()
                        }
                    }
                }
            )
    }


    fun chatGptRequest(callback: GptQuestionInterface, payload: JsonObject) {
        disposable = networkClient.askQuestion(NetworkConstants.ai_key, payload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    callback.onNetworkComplete(result)
                    destroyConnection()
                },
                { error ->
                    when {
                        retryCount < retryMax -> {
                            retryCount += 1
                            chatGptRequest(callback, payload)
                        }
                        else -> {
                            callback.onNetworkError(error)
                            destroyConnection()
                        }
                    }
                }
            )
    }

    fun dallERequest(callback: DallEInterface, payload: JsonObject) {
        disposable = networkClient.generateImage(NetworkConstants.ai_key, payload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    callback.onCompleted(result)
                    destroyConnection()
                },
                { error ->
                    when {
                        retryCount < retryMax -> {
                            retryCount += 1
                            dallERequest(callback, payload)
                        }
                        else -> {
                            callback.onFailed(error)
                            destroyConnection()
                        }
                    }
                }
            )
    }


    fun askQuestion(callback: GptQuestionInterface, model: String, temp: Double, messages: List<JsonObject>) {
        val payload = JsonObject()
        payload.addProperty("model", model)
        payload.addProperty("temperature", temp)

        val questionPayload: List<JsonObject> = messages
        payload.add("messages", Gson().toJsonTree(questionPayload))

        disposable = networkClient.askQuestion(NetworkConstants.ai_key, payload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    callback.onNetworkComplete(result)
                    destroyConnection()
                },
                { error ->
                    when {
                        retryCount < retryMax -> {
                            retryCount += 1
                            askQuestion(callback,model,  temp, messages)
                        }
                        else -> {
                            callback.onNetworkError(error)
                            destroyConnection()
                        }
                    }
                }
            )
    }


    /** ====== Retry Values======== **/
    var retryCount: Int = 0
    val retryMax: Int = 1

    fun destroyConnection(){
        disposable?.dispose()
    }

}