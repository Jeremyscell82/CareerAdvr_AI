package com.lloydsbyte.careeradvr_ai._intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.lloydsbyte.careeradvr_ai.MainActivity
import com.lloydsbyte.core.ErrorController
import com.lloydsbyte.core.customdialog.CustomDialogs
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.databinding.ActivityLandingBinding
import com.lloydsbyte.careeradvr_ai.utilz.GAccountHelper
import com.lloydsbyte.core.assetloader.AssetLoader
import com.lloydsbyte.core.custombottomsheet.ErrorBottomsheet
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.core.utilz.Utilz
import com.lloydsbyte.core.utilz.UtilzDateHelper
import com.lloydsbyte.network.ConfigModel
import com.lloydsbyte.network.NetworkClient
import com.lloydsbyte.network.NetworkConstants
import com.lloydsbyte.network.NetworkController
import com.lloydsbyte.network.interfaces.ConfigFileInterface
import timber.log.Timber

/**
 * This screen will show the intro fragments if the user has not seen them before,
 * This would also be the place one could display the onboarding section for first time users.
 */
class IntroSectionActivity: AppCompatActivity(), ConfigFileInterface {

    lateinit var binding: ActivityLandingBinding
    var isNewUser: Boolean = false
    var pullConfigInBackground: Boolean = false
    val networkClient by lazy {
        NetworkClient.create(NetworkConstants.serverBaseUrl)
    }
    val networkController by lazy {
        NetworkController(networkClient)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        binding = ActivityLandingBinding.inflate(layoutInflater)

        //Todo change this if not using FirebaseAuth
        if (false) {
//        if (StoredPref(this).isNewUser()) {
            //Run onboarding screens, and download config file in background
            setTheme(R.style.Theme_AppKit)
            setContentView(binding.root)
            pullConfigInBackground = true
            networkController.pullConfigFile(this)
            StoredPref(this).recordNewUser()
        } else {
            initiateApp()
        }
    }


    /**
     *  Sign In Code
     */
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
            StoredPref(this).recordNewUser()
            val user = FirebaseAuth.getInstance().currentUser //Dont need this
            ErrorController.logError("MainActivity -> SUCCESS -> ${FirebaseAuth.getInstance().currentUser?.uid}")
            val creationTime = UtilzDateHelper(UtilzDateHelper.DF_TIMEDATE).convertDateFromMillis(user?.metadata?.creationTimestamp!!)
            val loggedInTime = UtilzDateHelper(UtilzDateHelper.DF_TIMEDATE).convertDateFromMillis(user.metadata?.lastSignInTimestamp!!)
            isNewUser = creationTime == loggedInTime
            Timber.d("JL_ creation time: $creationTime logged in time: $loggedInTime")
            if (areTherePromos()){
                //Override new users and display the promo instead for all onboarding users
                binding.navHostContainer.findNavController().navigate(R.id.action_signin_to_promo)
            } else if (isNewUser){ //Takes a millisecond to log user in after creating account... it seems
                //Send user to bonus screen to get free tokens, send in new user amount
                binding.navHostContainer.findNavController().navigate(R.id.action_signin_to_newuser)
            } else {
                initiateApp()
            }
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            ErrorController.logError("MainActivity -> Something went wrong with sign in")
            val bottomsheet = ErrorBottomsheet.createInstance("Something has gone wrong", result.idpResponse?.error?.message?:"NA: Code: ${result.resultCode}")
            bottomsheet.show(supportFragmentManager, bottomsheet.tag)
        }
    }


    private fun areTherePromos(): Boolean {
        val configFile = Utilz.convertConfigToModel(StoredPref(this).readConfigFile())
        val promo = configFile.promoBanner
        var showIntroPromo: Boolean = false
        promo.map {
            if (it.active && it.id == 101) showIntroPromo = true
        }
        return showIntroPromo
    }

    fun initiateApp() {
        if (Utilz.isInDebugMode(this)){
            //Run this for debug mode
            val asset = AssetLoader().loadConfigFile(this)
            StoredPref(this).writeConfigFile(Utilz.convertConfigToString(asset))
            launchApp()
        } else {
            //If already pulled today, launch app
            if (UtilzDateHelper(UtilzDateHelper.DF_CAL).compareTodayDate(StoredPref(this).getLastPulledDate())){
                //User has already pulled today
                launchApp()
            } else {
                Toast.makeText(this, "Warming up server...", Toast.LENGTH_LONG).show()
                //Pull new config, for now use stored file in assets
                networkController.pullConfigFile(this)
            }
        }

    }
    fun launchApp() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        overridePendingTransition(
            R.anim.slide_in_up,
            R.anim.fade_out
        )
        finish()
    }

    fun showLoadingView(show: Boolean) {
        binding.loadingProgressbar.visibility = if (show) View.VISIBLE else View.GONE
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        CustomDialogs.launchDialog(
            sfm = supportFragmentManager,
            title = "Exit?",
            message = "Are you sure you want to exit now? We haven't even gotten started.",
            posText = "Exit",
            negText = "Cancel",
            okRun = Runnable { this@IntroSectionActivity.finish() },
            cancelRun = null,
            cancelable = true
        )
        if (false) super.onBackPressed()
    }

    override fun onComplete(configFile: ConfigModel.BaseStructure) {
        StoredPref(this).writeConfigFile(Utilz.convertConfigToString(configFile))
        StoredPref(this).setPullDate(UtilzDateHelper(UtilzDateHelper.DF_CAL).buildStrDate())
        if (!pullConfigInBackground){
            showLoadingView(false)
            launchApp()
        }
    }

    override fun onFailed(error: Throwable) {
        Timber.d("JL_ ERROR ON CONFIG FILE PULL: ${error.message}")
        //User stored pref from assets
        val asset = AssetLoader().loadConfigFile(this)
        StoredPref(this).writeConfigFile(Utilz.convertConfigToString(asset))
        if (!pullConfigInBackground){
            showLoadingView(false)
            launchApp()
        }
    }
}