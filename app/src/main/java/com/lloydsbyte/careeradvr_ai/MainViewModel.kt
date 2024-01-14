package com.lloydsbyte.careeradvr_ai

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.core.utilz.Utilz
import com.lloydsbyte.network.ConfigModel

class MainViewModel : ViewModel() {
    private var configFile: ConfigModel.BaseStructure? = null

    fun getDefaultPrompt(context: Context): String {
        if (configFile == null) {
            configFile = Utilz.convertConfigToModel(StoredPref(context).readConfigFile())
        }
        return configFile!!.defaultPrompt
    }

    fun getPromptListBusiness(context: Context): List<ConfigModel.Prompt> {
        if (configFile == null){
            configFile = Utilz.convertConfigToModel(StoredPref(context).readConfigFile())
        }
        return configFile!!.promptListBusiness
    }

    fun getPromptListPersonal(context: Context): List<ConfigModel.Prompt> {
        if (configFile == null){
            configFile = Utilz.convertConfigToModel(StoredPref(context).readConfigFile())
        }
        return configFile!!.promptListPersonal
    }
}