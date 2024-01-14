package com.lloydsbyte.core.search_bottomsheet

interface BottomsheetSearchInterface {
    fun onPhraseEntered(query: String)
    fun onCanceled()
}