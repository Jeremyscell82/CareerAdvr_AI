package com.lloydsbyte.careeradvr_ai

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.core.utilz.Utilz
import com.lloydsbyte.network.ConfigModel

class MainViewModel : ViewModel() {
    private var configFile: ConfigModel.BaseStructure? = null

    private fun loadConfigFile(context: Context) {
        configFile = Utilz.convertConfigToModel(StoredPref(context).readConfigFile())
    }

    fun getDefaultPrompt(context: Context): String {
        if (configFile == null) {
            loadConfigFile(context)
        }
        return configFile!!.defaultPrompt
    }


    fun getPromptList(context: Context): List<ConfigModel.Prompt> {
        if (configFile == null){
            loadConfigFile(context)
        }
        return configFile!!.promptList
    }
}