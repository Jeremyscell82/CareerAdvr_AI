package com.lloydsbyte.careeradvr_ai

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.lloydsbyte.careeradvr_ai.analytics.Analytix
import com.lloydsbyte.careeradvr_ai.analytics.MixPanelConstants
import com.lloydsbyte.careeradvr_ai.databinding.ActivityMainBinding
import com.lloydsbyte.careeradvr_ai.utilz.AdmobHelper
import com.lloydsbyte.core.ErrorController
import com.lloydsbyte.core.customdialog.CustomDialogs
import com.lloydsbyte.core.gcm.PushNotificationController
import com.lloydsbyte.core.permissions.PermissionCheck
import com.lloydsbyte.core.permissions.PermissionCheck.Companion.showSendToSettingsDialog
import com.lloydsbyte.careeradvr_ai.utilz.GAccountHelper
import com.lloydsbyte.careeradvr_ai.utilz.GptTokenController
import com.lloydsbyte.careeradvr_ai.utilz.IAP_Helper
import com.lloydsbyte.core.custombottomsheet.ForceUpdateBottomsheet
import com.lloydsbyte.core.custombottomsheet.VersionBottomsheet
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.core.utilz.Utilz
import com.lloydsbyte.core.utilz.UtilzDateHelper
import com.lloydsbyte.network.ConfigModel
import timber.log.Timber


open class MainActivity : IAP_Helper() {

    companion object {
        var permissionType: String = ""
    }

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var mInterstitialAd: InterstitialAd? = null
    lateinit var mainViewModel: MainViewModel
    var backStackBool = false
    private var backStackTimer = object : CountDownTimer(5000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            //We could do something here but... we don't
        }

        override fun onFinish() {
            //At this point let's call it quits and ask the user to check their settings.
            backStackBool = false
        }
    }


    /** PERMISSION CODE **/
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private var locationPermissionRequested: Boolean = false
    private var locationPermissionDenied: Boolean = false

    /** PUSH NOTIFICATIONS **/ // This val should stay on the MainActivity
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        //This will run in a loop so do not block the main thread or you will experience slow downs
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
            //Optionally you can save a value to StoredPrefs instead of requesting.... but it is recommended to request each time
        } else {

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        binding = ActivityMainBinding.inflate(layoutInflater)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setTheme(R.style.Theme_AppKit)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
//        setupNavBar()

//        initializeBilling()//Todo JL remove
        preparePermissions()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_controller) as NavHostFragment
        navController = navHostFragment.navController
        checkVersion()
    }

    private fun checkVersion() {
        val currentBuild = Utilz.getBuildNumber(this)
        val minBuild = Utilz.convertConfigToModel(StoredPref(this).readConfigFile()).minBuild
        val storedBuild = StoredPref(this).getStoredBuildVersion()

        if (currentBuild < minBuild) {
            //Show need to update dialog
            val bottomsheet = ForceUpdateBottomsheet.createInstance(
                resources.getString(R.string.force_update_title),
                resources.getString(R.string.force_update_message)
            )
            bottomsheet.show(supportFragmentManager, bottomsheet.tag)
        } else if (currentBuild != storedBuild) {
            //Show dialog
            val bottomsheetVersion = VersionBottomsheet.createInstance(
                resources.getString(
                    R.string.version_title,
                    Utilz.getVersionName(this@MainActivity)
                ),
                resources.getString(R.string.version_message),
                currentBuild
            )
            bottomsheetVersion.show(supportFragmentManager, bottomsheetVersion.tag)
        }
    }

//    private fun setupNavBar() {
//        /** REMOVE BELOW BLOCK OF CODE TO REMOVE BOTTOM BAR **/ //Don't forget to remove the code in the layout file
//        //Setup Bottombar and it's navHost controller
//        bottomNavBar = findViewById(R.id.bottom_nav)
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_controller) as NavHostFragment
//        navController = navHostFragment.navController
//        bottomNavBar.setupWithNavController(navController)
//        //Fix bottom bar nav from sticking
//        bottomNavBar.setOnItemSelectedListener { item ->
//            tabId = item.itemId
//            NavigationUI.onNavDestinationSelected(item, navController)
//            if (tabId == bottomNavBar.selectedItemId) {
//                navController.popBackStack(item.itemId, inclusive = false)
//            }
//            return@setOnItemSelectedListener true
//        }
//    }


    /** LOCATION PERMISSION CODE **/
    private fun preparePermissions() {
        //Setup Location Permissions
        locationPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        //Fine location has been granted
                        com.lloydsbyte.careeradvr_ai.MainActivity.Companion.permissionType =
                            "Fine location"
                    }

                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        //Approximate location has been granted
                        com.lloydsbyte.careeradvr_ai.MainActivity.Companion.permissionType =
                            "Coarse location"
                    }

                    else -> {
                        //Permission was denied, show the dialog and send to settings
                        if (locationPermissionDenied) {
                            showSendToSettingsDialog(
                                this,
                                resources.getString(R.string.permissions_dialog_title),
                                resources.getString(R.string.location_permission_dialog_message)
                            )
                        }
                    }
                }
                ErrorController.logStatus("JL_ user selected : $permissionType")
            }
    }

    fun requestLocationPermissionDialog() {
        CustomDialogs.launchDialog(
            sfm = supportFragmentManager,
            title = resources.getString(R.string.location_dialog_title),
            message = resources.getString(R.string.location_dialog_message),
            posText = resources.getString(R.string.location_dialog_pos),
            negText = resources.getString(R.string.location_dialog_neg),
            okRun = { requestLocationPermission() },
            cancelRun = null,
            true
        )
    }

    private fun requestLocationPermission() {
        locationPermissionRequested = true
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestPushNotificationPermission() {
        //Ensure to ask the controller if user hasPermissionsBeenApproved() before calling this
        val runnable = Runnable {
            CustomDialogs.launchDialog(
                supportFragmentManager,
                resources.getString(R.string.notification_permission_title),
                resources.getString(R.string.notification_permission_message),
                resources.getString(R.string.notification_permission_ok),
                resources.getString(R.string.notification_permission_cancel),
                { requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                { },
                true
            )
        }
        PushNotificationController().askNotificationPermission(
            this,
            runnable,
            requestPermissionLauncher
        )
    }

    /** Sign In Code **/
    fun launchSignIn() {
        binding.loadingProgressbar.visibility = View.VISIBLE
        GAccountHelper.createSignInIntent(signInLauncher)
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        binding.loadingProgressbar.visibility = View.GONE
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser //Dont need this
            ErrorController.logError("MainActivity -> SUCCESS")
            val creationTime =
                UtilzDateHelper(UtilzDateHelper.DF_TIMEDATE).convertDateFromMillis(user?.metadata?.creationTimestamp!!)
            val loggedInTime =
                UtilzDateHelper(UtilzDateHelper.DF_TIMEDATE).convertDateFromMillis(user.metadata?.lastSignInTimestamp!!)
            Timber.d("JL_ creation time: $creationTime logged in time: $loggedInTime")
            if (creationTime == loggedInTime) { //Takes a millisecond to log user in after creating account... it seems
                //This is the users first time creating an account
                Toast.makeText(this, "WELCOME!", Toast.LENGTH_SHORT).show()
            } else {
                //This is a returning user
                Toast.makeText(this, "WELCOME BACK!", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            ErrorController.logError("MainActivity -> Something went wrong with sign in")
        }
    }


    /** ADMOB CODE **/
    var doubleAdShown: Boolean = false
    fun fireOffAd() {
        Timber.d("JL_ firing off ad...")
        loadUpAd()
        mInterstitialAd?.show(this@MainActivity)
    }

    private fun loadUpAd() {
        MobileAds.initialize(
            this
        ) {}
        //Set the InterstitialAd
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Analytix().reportAdClicked(applicationContext)
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Timber.d("JL_ ad has been dismissed")
                mInterstitialAd = null
                initializeAds()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                Analytix().reportAdFailed(applicationContext)
//                mInterstitialAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Analytix().reportAdShown(applicationContext)
            }
        }
    }

    private fun initializeAds() {
        val adRequest = AdRequest.Builder().build()
        val runInDebug = Utilz.isInDebugMode(this)
        val adId = if (runInDebug) AdmobHelper.testUnitId else AdmobHelper.unitId
        InterstitialAd.load(this, adId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                ErrorController.logError(adError.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }

    /** Download Functions **/
    fun downloadImage(url: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            downloadFile("dall-e_chap-e-${Utilz.getRandomDigits()}.jpg", "Dall-E Image", url)
        } else {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) downloadFile(
                    "dall-e_chap-e-${Utilz.getRandomDigits()}.jpg",
                    "Dall-E Image",
                    url
                )
            }
        }
    }

    private fun downloadFile(fileName: String, desc: String, url: String) {
        // fileName -> fileName with extension
        val request = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName)
            .setDescription(desc)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadID = downloadManager.enqueue(request)
    }


    /** Loading Progress Bar **/
    fun showLoadingView(show: Boolean) {
        binding.loadingProgressbar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        GptTokenController().shouldResetTokens(this)
        initializeAds()
        initializeBilling()
        
        //If one wants to handle the permission request, here is where one can do so
        if (locationPermissionRequested && PermissionCheck.isLocationGranted(this)) {
            CustomDialogs.snackbar(binding.root, "Location Permission Granted")
        } else if (locationPermissionRequested) {
            locationPermissionDenied = true
        }
        locationPermissionRequested = false



    }


    @SuppressLint("RestrictedApi")
    override fun onBackPressed() {
        if (navController.currentBackStack.value.size == 2 && backStackBool) {
            finish()
        } else if (navController.currentBackStack.value.size == 2) {
            CustomDialogs.snackbar(binding.root, "Press back again to exit")
            backStackBool = true
            backStackTimer.start()

        } else {
            navController.popBackStack()
        }
        if (false) super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        backStackTimer.cancel()
    }


    /**
     * Getters for the config file
     */

    fun getListOfPrompts(): List<ConfigModel.Prompt> {
        return mainViewModel.getPromptList(this)
    }

    fun getDefaultPrompt(): String {
        return mainViewModel.getDefaultPrompt(this)
    }

    fun getSecretKey(): String {
        return mainViewModel.getSecretKey(this)
    }
}
