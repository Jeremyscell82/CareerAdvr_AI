package com.lloydsbyte.database.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "chatHeaderModel", indices = [Index(value = ["timestamp"], unique = true)])
data class ChatHeaderModel(
    @PrimaryKey(autoGenerate = true)
    var dbKey: Long,
    @ColumnInfo(name = "gpt_id")
    val chatGptConvoId: String,
    @ColumnInfo(name = "conversation_name")
    val convoName: String,
    @ColumnInfo(name = "timestamp")
    val conversationID: Long,
    @ColumnInfo(name = "category")
    val category: String
): Parcelable
