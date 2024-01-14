package com.lloydsbyte.database

import android.content.Context
import com.lloydsbyte.database.examples.CountryModel
import com.lloydsbyte.database.models.ChatHeaderModel
import com.lloydsbyte.database.models.ChatModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DatabaseController(
    private val databaseInterface: DatabaseInterface
){

    fun addChat(applicationContext: Context, chatMessage: ChatModel) {
        AppDatabase.getDatabase(applicationContext).chatHistoryDao().addChatMessage(chatMessage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
            }).let {
            }
    }

    fun addHeader(applicationContext: Context, chatHeader: ChatHeaderModel) {
        AppDatabase.getDatabase(applicationContext).chatHistoryDao().addConversationHeader(chatHeader)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
            }).let {
            }
    }

    fun addConversation(applicationContext: Context, conversation: List<ChatModel>, chatHeaderModel: ChatHeaderModel){
        AppDatabase.getDatabase(applicationContext).chatHistoryDao().addConversationHeader(chatHeaderModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
            }).let {
                addConvoToDB(applicationContext, conversation)
            }
    }

    private fun addConvoToDB(applicationContext: Context, conversation: List<ChatModel>){
        AppDatabase.getDatabase(applicationContext).chatHistoryDao().addConversation(conversation)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
            }).let {
                //Todo could keep it on schedulers thread, but decided not to
                databaseInterface.onComplete()
            }
    }

    fun removeConversation(applicationContext: Context, chatHeaderModel: ChatHeaderModel) {
        AppDatabase.getDatabase(applicationContext).chatHistoryDao().deleteConversation(chatHeaderModel.conversationID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
            }).let {
                removeConversationHeader(applicationContext, chatHeaderModel)
            }
    }

    private fun removeConversationHeader(applicationContext: Context, chatHeaderModel: ChatHeaderModel){
        AppDatabase.getDatabase(applicationContext).chatHistoryDao().deleteChatHeader(chatHeaderModel.dbKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
            }).let {
                databaseInterface.onComplete()
            }
    }


    //Clear the database first, then populate it once it has been cleared
    fun replaceCountryListDatabase(
        applicationContext: Context,
        countryModelList: List<CountryModel>
    ) {
//        AppDatabase.getDatabase(applicationContext).countryDao().deleteLocationDB()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//            }, {
//            }).let {
//                //Todo could keep it on schedulers thread, but decided not to
//                populateDatabase(applicationContext, countryModelList)
//            }
    }

    private fun populateDatabase(applicationContext: Context, countryList: List<CountryModel>) {
//        AppDatabase.getDatabase(applicationContext).countryDao().addCountriesData(countryList)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//            }, {
//            }).let {
//                databaseInterface.onComplete()
//            }
    }

}