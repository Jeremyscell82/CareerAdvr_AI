package com.lloydsbyte.careeradvr_ai.history

import androidx.lifecycle.ViewModel
import com.lloydsbyte.database.AppDatabase
import com.lloydsbyte.database.models.ChatHeaderModel
import com.lloydsbyte.database.models.ChatModel
import io.reactivex.Flowable

class HistoryViewModel: ViewModel() {


    fun getChatHistory(appDatabase: AppDatabase): Flowable<List<ChatHeaderModel>> {
        //Gets the headers from the db
        return appDatabase.chatHistoryDao().getConversationHeaders()
    }


    /** ViewModel Section for the conversation Page **/
    var convoHeaderModel: ChatHeaderModel? = null
    var convoCarbonCopy: List<ChatModel> = emptyList()
    fun getConversation(appDatabase: AppDatabase, convoId: Long): Flowable<List<ChatModel>> {
        //Gets the headers from the db
        return appDatabase.chatHistoryDao().getConversation(convoId)
    }

}