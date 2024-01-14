package com.lloydsbyte.careeradvr_ai

/**
 * This class is where all keys are tied to,
 * this class also has pointers to turning on certain features
 */
class DEV_DOCS {

    //KEYS
    companion object {
        //Admob ID's
        const val JL_APPID: String = "ca-app-pub-8308745676637485~3282161859"
        const val demoAppId: String = "ca-app-pub-3940256099942544~3347511713" //This is a demo key currently being used to allow the app to build, read Admob comments below
        const val unitId: String = "ca-app-pub-8308745676637485/3689355965"
        const val testUnitId = "ca-app-pub-3940256099942544/1033173712"

        const val mixPanelID: String = "" //This key is found in the analytics module, in the class MixPanelController (line 52)

        const val ai_key ="Bearer key_here"// This key lives in the network module, in the NetworkConstants class (line 7). If you want to use the chatGpt feature you must add your key similar to how it currently is formatted
    }


    /** =========== Intro ===========
     *  To enable this class, make a change in the AndroidManifest file to launch the intro activity first.
     *  Basically move the <intent-filter... from the main activity to the intro activity and it will launch for first time users.
     */

/** =========== AdMob ===========
     * In the AndroidManifest for app, on line 40 you will see APPLICATION_ID.
     * Replace the android:value="" with your application id.
     * This is given to you when creating the app on Admob.com
     * Above are the areas where you would put the unit id, I keep the appid here although it is not required.
     * Currently there is a demo id I found on the internet, I do not recommend using it... it just allows to build the app w/o setting it up.
     * A Copy of the id is above in the demoAppId string
     */

    /**  =========== Google Services (google-services.json)  ===========
     *  Go through the steps on https://console.firebase.google.com/ to create a new project
     *  Download the google-services.json, place it in the proper place.
     *  In the Gradle (:app) version, uncomment the pluggin google.services
     *  To test this switch the enableGoogleServices boolean to true in the DebugFragment (line 38)
     */

    /**  =========== Firebase Crashlytics  ===========
     * Uncomment the plugin at the top of the Gradle (:app),
     * id 'com.google.firebase.crashlytics'
     * Test the service by adding below to a button or oncreate of a fragment
     * throw RuntimeException("Test Crash")
     */

    /**  =========== Firebase Performance  ===========
     * Uncomment the plugin at the top of the Gradle (:app)
     * id 'com.google.firebase.firebase-perf'
     * Run the application and see the results in the Console shortly
     * ** DO NOT FORGET ** switch the logger found in the Android Manifest (line 38) to false
     *
     */

    /**  =========== Google In App Purchases  ===========
     *  Google Services must be enabled for this to work
     * All code has already been integrated, all you need to do is add the products in your console
     * https://support.google.com/googleplay/android-developer/answer/1153481?hl=en#zippy=%2Ccreate-a-single-in-app-product
     * Once created, add the sku names in the IAP_Helper (function on line 62). Replace all "" with your sku, only fill what you will need and remove the rest of the example QueryProductDetailsParams.
     */

    /**  =========== Room Database  ===========
     *  This is all already configured, to use all you should do is replace the DB Name
     *  This value could be found in the database module, in the class AppDatabase. Line 16, change the name to your own, keep the recommended format
     *  Add your own Dao and Model (check out examples folder), don't forget to add the dao to the AppDatabase
     */

    /**  =========== Open AI's chatGPT ===========
     * This app already has the api connected, the basics have been laid out. From here you can add your own logic and token counter of choice
     * To use this feature, create your api key on openAi's website and place the key in the network module, in the class NetworkConstants (line 7)
     */

    /**  =========== Analytics ===========
     *  You have two options, firebase does an excellent job with it's own events built in that track basic functionality.
     *  So I like to put all custom analytic events on mixpanel (for pricing and other reasons), this could be configured in the MixPanelController
     *  ** DO NOT FORGET ** to create your account and add the key in the controller
     */
}