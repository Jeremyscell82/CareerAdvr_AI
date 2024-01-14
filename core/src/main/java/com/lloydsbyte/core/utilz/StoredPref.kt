package com.lloydsbyte.core.utilz

import android.content.Context
import android.content.SharedPreferences
import com.lloydsbyte.network.NetworkConstants

/**
 * Store temporary values in storage quickly and easily from anywhere in the app that has access to context.
 * This design allow you to easily add/remove items with out having to search the entire app for them.
 */
class StoredPref(
    val context: Context
) {
    /** SHARED PREFERENCE KEYS **/
    private val NEW_USER =
        "appkit.newuser" //This can also be done using Firebase Realtime Database... contact me for help on this as it is not in this demo
    private val USER_NAME = "appkit.username"
    private val CONFIG_FILE = "appkit.config_file"

    /** PROFILE **/
    private val PROFILE_STATUS = "appkit.gpt.profile.status"
    val PROFILE_NAME = "appit.gpt.profile.name"
    val PROFILE_AGE = "appit.gpt.profile.age"
    val USAGE_LIMIT = "appkit.gpt.usage.limit"
    val USAGE_DATE = "appkit.gpt.usage.date"

    /** ChatGPT **/
    private val GPT_MODEL = "appkit.gpt.model"
    private val GPT_4_MODEL_PREVIEW = "appkit.gpt.model_preview"
    private val GPT_TEMP = "appkit.gpt.temperature"
    private val GPT_STREAM = "appkit.gpt.stream"
    private val GPT_TOKENS_USED = "appkit.gpt.tokens_used"

    /** TOKENS **/
    private val GPT_TOKEN_COUNTER = "appkit.gpt_token_counter"
    private val GPT_TEMPERATURE = "appkit.gpt_temperature"

    private val SHOW_ADS = "appkit.show_ads"
    private val LAST_PULLED = "appkit.last_pulled_date"
    private val LAST_RATED_CONVO = "appkit.last_rated_date"
    private val FILE_VERSION = "appkit.file_version"
    private val MIN_VERSION = "appkit.min_version"
    private val SAVED_VERSION = "appkit.saved_version"

    //To avoid asking the user multiple times for permissions, this could easily save a users preferences
    private val HAS_DENIED_NOTIFICATIONS = "appkit.denied.notifications"

    /** Shared Preference Functions **/
    fun writeToStored() {
        writeString("KEY", "VALUE")
        writeInt("KEY", 0)
        writeLong("KEY", 0L)
        writeBool("KEY", false)
    }

    //#nifty
    fun readFromStored() {
        readString(key = "KEY", default = "VALUE")
        readInt(key = "KEY", defaultValue = 0)
        readLong(key = "KEY")
        readBool(key = "KEY", default = false)
    }

    /** OnBoarding **/
    fun recordNewUser() {
        writeBool(NEW_USER, false)
    }

    fun isNewUser(): Boolean {
        return readBool(NEW_USER, true)
    }


    fun readConfigFile(): String {
        return readString(CONFIG_FILE, "")
    }

    fun writeConfigFile(configFile: String) {
        writeString(CONFIG_FILE, configFile)
    }

    fun updateTokenCount(count: Int) {
        writeInt(GPT_TOKEN_COUNTER, count)
    }

    fun getTokenCount(): Int {
        return readInt(GPT_TOKEN_COUNTER, 0)
    }

    /** Ads **/
    fun showAds(): Boolean {
        return readBool(SHOW_ADS, true)
    }

    fun disableAds(value: Boolean) {
        writeBool(SHOW_ADS, value)
    }

    /** PERMISSIONS **/
    fun hasDeniedPermissions(): Boolean {
        return readBool(HAS_DENIED_NOTIFICATIONS, false)
    }

    fun denyNotifications() {
        writeBool(HAS_DENIED_NOTIFICATIONS, true)
    }

    /** ENSURE THE APP PULLS ONCE PER DAY **/
    //Config file
    fun getLastPulledDate(): String {
        return readString(LAST_PULLED, "")
    }

    fun setPullDate(dateInStr: String) {
        writeString(LAST_PULLED, dateInStr)
    }

    //Rate the conversation, once per day
    fun getLastRatedDate(): String {
        return readString(LAST_RATED_CONVO, "")
    }

    fun setLastRatedDate(dateInStr: String) {
        writeString(LAST_RATED_CONVO, dateInStr)
    }

    /** PROFILE CALLS **/
    fun getProfileStatus(): String {
        return readString(PROFILE_STATUS, "FREE")
    }

    fun setProfileStatus(status: String) {
        writeString(PROFILE_STATUS, status)
    }

    fun getUserAge(): String {
        return readString(PROFILE_AGE, "0")
    }

    fun setUserAge(age: String) {
        writeString(PROFILE_AGE, age)
    }

    fun getUserName(): String {
        return readString(PROFILE_NAME, "Boss")
    }

    fun setUserName(name: String) {
        writeString(PROFILE_NAME, name)
    }

    fun setUsageLimit(usage: Int) {
        writeInt(USAGE_LIMIT, usage)
    }
    fun getUsageLimit(): Int {
        return readInt(USAGE_LIMIT, 0)
    }

    fun setUsageDate(date: Long) {
        writeLong(USAGE_DATE, date)
    }
    fun getUsageDate(): Long {
        return readLong(USAGE_DATE)
    }

    /** CONFIG FILE **/
    fun storeMinVersion(version: Int) {
        writeInt(MIN_VERSION, version)
    }

    fun readMinVersion(): Int {
        return readInt(MIN_VERSION, 1)
    }

    fun storeFileVersion(version: String) {
        writeString(FILE_VERSION, version)
    }

    fun readFileVersion(): String {
        return readString(FILE_VERSION, "")
    }

    /** Chat GPT **/
    fun useGpt4Turbo(): Boolean {
        return readBool(GPT_4_MODEL_PREVIEW, false)
    }

    fun setGpt4TurboValue(value: Boolean) {
        writeBool(GPT_4_MODEL_PREVIEW, value)
    }

    fun getChatModel(): String {
        return readString(GPT_MODEL, NetworkConstants.default_gpt_model)
    }

    fun setChatModel(model: String) {
        writeString(GPT_MODEL, model)
    }

    fun chatTemp(): Double {
        return readString(GPT_TEMP, NetworkConstants.temperature.toString()).toDouble()
    }

    fun setChatTemp(temp: Double) {
        writeString(GPT_TEMP, temp.toString())
    }


    //For the new version calls
    fun setStoredBuildNum(version: Int) {
        writeInt(SAVED_VERSION, version)
    }

    fun getStoredBuildVersion(): Int {
        return readInt(SAVED_VERSION, 0)
    }


    /** ============= GENERIC METHODS TO BE USED WITH WRAPPER ============== **/
    private val PRIVATE_MODE = 0
    private val GLOBAL_KEY = "com.lloydsbyte.weatherapp.prefs"
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(GLOBAL_KEY, PRIVATE_MODE)

    private fun readString(key: String, default: String): String {
        return sharedPref.getString(key, null) ?: default
    }

    //Made public for edit field bottomsheet
    fun writeString(key: String, value: String) {
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun readBool(key: String, default: Boolean): Boolean {
        return sharedPref.getBoolean(key, default)
    }

    private fun writeBool(key: String, value: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun readInt(key: String, defaultValue: Int): Int {
        return sharedPref.getInt(key, defaultValue)
    }

    private fun writeInt(key: String, value: Int) {
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    private fun readLong(key: String): Long {
        return sharedPref.getLong(key, 0L)
    }

    private fun writeLong(key: String, value: Long) {
        val editor = sharedPref.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    private fun clearAllValues() {
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }
}