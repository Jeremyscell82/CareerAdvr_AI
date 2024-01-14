package com.lloydsbyte.core.assetloader

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lloydsbyte.network.ConfigModel
import java.io.IOException
import java.lang.reflect.Type


/**
 * This class has one purpose, to read, convert, and return the local json file as a model
 * To start the demo we will store the config file in assets and read it on launch
 */
class AssetLoader {

    fun loadConfigFile(context: Context): ConfigModel.BaseStructure {
        val catalogJsonString = getJsonDataFromAsset(context = context, fileName = "accubooks_config.json")
        val type: Type = object : TypeToken<ConfigModel.BaseStructure?>(){}.type
        return Gson().fromJson(catalogJsonString, type)
    }

    fun loadLocalConfig(context: Context): AppConfigModel.AppConfigFileResponse {
        val catalogJsonString = getJsonDataFromAsset(context = context, fileName = "appkit_config.json")
        val type: Type = object : TypeToken<AppConfigModel.AppConfigFileResponse?>(){}.type
        return Gson().fromJson(catalogJsonString, type)
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

}