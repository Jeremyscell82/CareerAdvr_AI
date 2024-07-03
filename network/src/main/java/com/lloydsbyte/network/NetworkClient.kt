package com.lloydsbyte.network

import com.google.gson.JsonObject
import com.lloydsbyte.network.examples.CountryResponse
import com.lloydsbyte.network.responses.ChatGptResponse
import com.lloydsbyte.network.responses.DallEResponse
import com.lloydsbyte.network.responses.ModelResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface NetworkClient {
    companion object {

        //Headers example
        private fun prepHeaders(headerOne: String, headerTwo: String): OkHttpClient.Builder {
            return OkHttpClient.Builder().apply {
//                addInterceptor(
//                    Interceptor { chain ->
//                        val builder = chain.request().newBuilder()
//                        builder.header("headerOne", headerOne)
//                        builder.header("headerTwo", headerTwo)
//                        return@Interceptor chain.proceed(builder.build())
//                    }
//                )
                connectTimeout(120, TimeUnit.SECONDS)
                writeTimeout(120, TimeUnit.SECONDS)
                readTimeout(120, TimeUnit.SECONDS)
            }
        }

        //Example to use the headers
        fun createWithHeaders(
            baseUrl: String,
            headerOne: String,
            headerTwo: String
        ): NetworkClient {
            val client = prepHeaders(headerOne, headerTwo)
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE) //Enable while debugging ONLY
            client.addInterceptor(interceptor)

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .baseUrl(baseUrl)
                .build()

            return retrofit.create(NetworkClient::class.java)
        }

        private fun buildClient(): OkHttpClient.Builder {
            return OkHttpClient.Builder().apply {
                connectTimeout(60, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
                readTimeout(60, TimeUnit.SECONDS)
            }
        }

        fun create(baseUrl: String): NetworkClient {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildClient().build())
                .baseUrl(baseUrl)
                .build()
            return retrofit.create(NetworkClient::class.java)
        }
    }

    /** CONFIG FILE CALLS **/
    @Headers("Content-Type: application/json")
    @GET(NetworkConstants.configFileEndpoint)
    fun getConfigFile(): Observable<ConfigModel.BaseStructure>

    @Headers("Content-Type: application/json")
    @GET("all")
    fun getCountyList(): Observable<List<CountryResponse.CountryResponse>>

    /** CHAT GPT CALLS **/
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET(NetworkConstants.aiModelsEndpoint)
    fun getModels(
        @Header("Authorization") token: String,
    ): Observable<ModelResponse.ModelData>

    @Headers("Content-Type: application/json")
    @POST(NetworkConstants.chatGptEndpoint)
    fun askQuestion(
        @Header("Authorization") token: String,
        @Body body: JsonObject
    ): Observable<ChatGptResponse.GptData>

    @Headers("Content-Type: application/json")
    @POST(NetworkConstants.imageGptEndpoint)
    fun generateImage(
        @Header("Authorization") token: String,
        @Body body: JsonObject
    ): Observable<DallEResponse.DallEData>

}