package com.lloydsbyte.careeradvr_ai.chat

import androidx.lifecycle.ViewModel
import com.lloydsbyte.core.utilz.UtilzDateHelper
import com.lloydsbyte.database.models.ChatHeaderModel
import com.lloydsbyte.database.models.ChatModel

class ChatViewModel: ViewModel() {

    //For ALL
    var isUserSubscribed: Boolean = false
    var chatTitle: String = ""
    var systemPrompt: String = ""
    var instructions: String = ""
    var showInstructions: Boolean = true
    var gptConvoId: String = ""
    var convoCost: Int = 0
    var questionEntered: String = ""
    var convoHeaderModel: ChatHeaderModel? = null
    var chatThread: MutableList<ChatModel> = mutableListOf()
    private var convoTimeStamp: Long = 0L
    var isMockInterview: Boolean = false

    fun getConvoTimeStampId(): Long {
        if (convoTimeStamp == 0L){
            convoTimeStamp = if (convoHeaderModel?.conversationID != null){
                convoHeaderModel!!.conversationID
            } else {
                UtilzDateHelper(UtilzDateHelper.DF_TIMEDATE).buildMillisDate()
            }
        }
        return convoTimeStamp
    }


    //For AMA
    var isAma: Boolean = false




}