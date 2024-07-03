package com.lloydsbyte.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.lloydsbyte.database.models.ChatHeaderModel
import com.lloydsbyte.database.models.ChatModel
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface ChatHistoryDao {

    @Insert
    fun addConversation(conversation: List<ChatModel>): Completable

    @Insert
    fun addChatMessage(chatMessage: ChatModel): Completable

    @Insert(onConflict = REPLACE)
    fun addConversationHeader(conversationHeader: ChatHeaderModel): Completable

    @Query("SELECT * FROM chatmodel WHERE conversation_id IS :convoId")
    fun getConversation(convoId: Long): Flowable<List<ChatModel>>

    @Query("SELECT * FROM chatheadermodel")
    fun getConversationHeaders(): Flowable<List<ChatHeaderModel>>

    @Query("DELETE FROM chatheadermodel WHERE dbKey IS :dbKey")
    fun deleteChatHeader(dbKey: Long) : Completable

    @Query("DELETE FROM chatmodel WHERE conversation_id IS :convoId")
    fun deleteConversation(convoId: Long) : Completable


}