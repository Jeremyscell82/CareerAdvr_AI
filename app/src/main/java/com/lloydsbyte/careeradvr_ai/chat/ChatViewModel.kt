package com.lloydsbyte.careeradvr_ai.chat

import androidx.lifecycle.ViewModel
import com.lloydsbyte.careeradvr_ai.utilz.CategoryHelper
import com.lloydsbyte.core.utilz.UtilzDateHelper
import com.lloydsbyte.database.models.ChatHeaderModel
import com.lloydsbyte.database.models.ChatModel
import timber.log.Timber

class ChatViewModel: ViewModel() {

    //For ALL
    var chatTitle: String = ""
    var systemPrompt: String = ""
    var promptCategory: Int = CategoryHelper.AMA
    var gptConvoId: String = ""
    var convoCost: Int = 0
    var questionEntered: String = ""
    var convoHeaderModel: ChatHeaderModel? = null
    var chatThread: MutableList<ChatModel> = mutableListOf()
    private var convoTimeStamp: Long = 0L

    fun getConvoTimeStampId(): Long {
        if (convoTimeStamp == 0L){
            Timber.d("JL_ timestamp was empty....$convoTimeStamp")
            convoTimeStamp = if (convoHeaderModel?.conversationID != null){
                convoHeaderModel!!.conversationID
            } else {
                UtilzDateHelper(UtilzDateHelper.DF_TIMEDATE).buildMillisDate()
            }
        }
        Timber.d("JL_ initiating timestamp feed....$convoTimeStamp")
        return convoTimeStamp
    }


    //For AMA
    var isAma: Boolean = false




}