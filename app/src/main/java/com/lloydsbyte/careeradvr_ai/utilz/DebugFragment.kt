package com.lloydsbyte.careeradvr_ai.utilz

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.databinding.FragmentDebugBinding
import com.lloydsbyte.core.ErrorController
import com.lloydsbyte.core.customdialog.CustomDialogs
import com.lloydsbyte.core.gcm.PushNotificationController
import com.lloydsbyte.core.permissions.PermissionCheck
import com.lloydsbyte.core.permissions.location.LocationController
import com.lloydsbyte.core.permissions.location.LocationControllerListener
import com.lloydsbyte.core.selection_bottomsheet.BottomSheetHelper
import com.lloydsbyte.core.selection_bottomsheet.BottomsheetSelectionFragment
import com.lloydsbyte.core.utilz.ModelConverter
import com.lloydsbyte.database.DatabaseController
import com.lloydsbyte.database.DatabaseInterface
import com.lloydsbyte.network.interfaces.GptModelInterface
import com.lloydsbyte.network.NetworkClient
import com.lloydsbyte.network.NetworkConstants
import com.lloydsbyte.network.NetworkController
import com.lloydsbyte.network.examples.CountryInterface
import com.lloydsbyte.network.examples.CountryResponse
import com.lloydsbyte.network.responses.ModelResponse
import io.reactivex.disposables.Disposable

class DebugFragment : Fragment(), CountryInterface, DatabaseInterface, GptModelInterface,
    BottomSheetHelper.BottomsheetSelectionInterface, LocationControllerListener {


    val enableGoogleServices: Boolean = false

    lateinit var binding: FragmentDebugBinding
    val networkClient by lazy {
        NetworkClient.create(NetworkConstants.countriesApi)
    }

    val networkController by lazy {
        NetworkController(networkClient)
    }

    var countriesList: List<CountryResponse.CountryResponse> = emptyList()
    var databaseDisposable: Disposable? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDebugBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            addLog("Hello, what would you like to test out today?")
            debugClearTerminalFab.setOnClickListener {
                resetLog()
            }
            debugCore.setOnClickListener {
//                locationTest()
//                testGCM()
//                testOutAds()
//                testSelectableBottomsheet(true)
//                testLocation()
                testCameraPermission()
            }
            debugNetwork.setOnClickListener {
                testNetwork()
            }
            debugDatabase.setOnClickListener {
                testDatabase()
            }
            if (enableGoogleServices)changeSignInButton() else debugSignin.visibility = View.GONE
            debugSignin.setOnClickListener {
                if (enableGoogleServices) {
                  //To prevent crashes, this is wrapped in a if statement
                    if (FirebaseAuth.getInstance().currentUser == null) {
                        //User is not signed in, sign them in
                        (requireActivity() as com.lloydsbyte.careeradvr_ai.MainActivity).launchSignIn()
                    } else {
                        CustomDialogs.launchDialog(
                            sfm = requireActivity().supportFragmentManager,
                            title = "Signing out?",
                            message = "Tokens are synced to your Google account, with out being signed in you will not be able to purchase tokens.",
                            posText = "Sign Out",
                            negText = "Cancel",
                            okRun = Runnable {
                                changeSignInButton()
                                GAccountHelper.signOutUser(requireActivity(), Runnable {
                                    Toast.makeText(
                                        requireActivity(),
                                        "Signed out successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                })
                            },
                            cancelRun = null,
                            cancelable = false
                        )
                    }
                }
            }
            debugGpt.setOnClickListener {
                addLog("Launching GPT interface...good luck")
//                findNavController().navigate(R.id.action_debugFragment_to_gptFragment)
            }
            debugIap.setOnClickListener {
                addLog("Launching IAP helper")
//                findNavController().navigate(R.id.action_debugFragment_to_purchaseFragment)
            }

            //For database testing
//            databaseDisposable = readCountriesFromDB(
//                AppDatabase.getDatabase(requireActivity().applicationContext)
//            )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { countries ->
//                        addLog("Reading ${countries.size} # of entries in DB")
//                    },
//                    { error ->
//                        ErrorController.logError(error.message)
//                    }
//                )

        }
    }

    private fun changeSignInButton() {
        val copy =
            if (FirebaseAuth.getInstance().currentUser == null) resources.getString(R.string.debug_signin) else resources.getString(
                R.string.debug_signout
            )
        binding.debugSignin.text = copy
    }

    /** CORE FUNCTIONALITY TESTS **/
    private fun testLocation() {
        //Use this button to load up all core components
        if (PermissionCheck.isLocationGranted(requireActivity())) {
            addLog("Permission for location was granted ... checking for location")
            addLog("This could take up to a minute. Be patient")
            LocationController(requireActivity(), this).getLocationInfo(Runnable { CustomDialogs.snackbar(binding.root, "You said no :( ") })
        } else {
            addLog("Granting permission for location\n...look for the result in a snackbar.\nThen press location again")
            (requireActivity() as com.lloydsbyte.careeradvr_ai.MainActivity).requestLocationPermissionDialog()
        }
    }

    private fun testCameraPermission() {
        if (PermissionCheck.isCameraGranted(requireActivity())){
            addLog("Permission for Camera has been granted")
        } else {
            addLog("Requesting permission for Camera")
            PermissionCheck.requestPermission(requireActivity(), Manifest.permission.CAMERA, false) //permissionDenied should be a value stored to help the user go to settings once it has been denied
        }
    }
    private fun testGCM() {
        //Test push notifications from Google Cloud Messenger
        if (PushNotificationController().hasPermissionsBeenApproved(requireActivity())) {
            addLog("Permission for GCM has been approved already")
        } else {
            addLog("Requesting Permission for GCM")
            (requireActivity() as com.lloydsbyte.careeradvr_ai.MainActivity).requestPushNotificationPermission()
        }
    }

    private fun testOutAds() {
        addLog("Testing ads")
        (requireActivity() as com.lloydsbyte.careeradvr_ai.MainActivity).fireOffAd()
    }

    private fun testNetwork() {
        addLog("Testing network code....")
        networkController.pullCountriesApi(this)
    }

    private fun testDatabase() {
        if (countriesList.isNotEmpty()) {
            addLog("Testing Database entry")
            DatabaseController(this).replaceCountryListDatabase(
                requireActivity().applicationContext,
                ModelConverter.convertCountryResponse(countriesList)
            )
        } else {
            addLog("Perform the Network test before this test")
        }
    }

    private fun testSelectableBottomsheet(singleSelect: Boolean) {
        val arrayToUse = resources.getStringArray(R.array.debug_selectable_array)
        val bottomsheet = BottomsheetSelectionFragment.createInstance(callback = this, title = null, items = BottomSheetHelper().createBottomSheetList(arrayToUse.toList()), singleSelect = singleSelect)
        bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
    }


    /** TERMINAL FUNCTIONALITY **/
    private fun addLog(log: String) {
        val terminal = when {
            binding.debugTerminalLayout.debugTerminalConsole.text.isEmpty() || binding.debugTerminalLayout.debugTerminalConsole.text.endsWith(
                ">_"
            ) -> {
                "$ $log"
            }

            else -> {
                "$ $log \n${binding.debugTerminalLayout.debugTerminalConsole.text.toString()}"
            }
        }
        binding.debugTerminalLayout.debugTerminalConsole.text = terminal

    }

    private fun resetLog() {
        binding.debugTerminalLayout.debugTerminalConsole.text = ">_"
    }

    /** Network Call Response **/
    override fun onPullCompleted(countryResponseList: List<CountryResponse.CountryResponse>) {
        countriesList = countryResponseList
        addLog("SUCCESS, ${countriesList.size} countries collected")
    }

    override fun onPullFailed(error: Throwable) {
        ErrorController.captureError("DebugFragment-Apidemo", error)
        addLog("Something has gone wrong...")
        addLog(error.localizedMessage)
    }

    override fun onComplete() {
        addLog("SUCCESS, Data was saved to the DB")
    }

//    private fun readCountriesFromDB(
//        appDatabase: AppDatabase
//    ): Flowable<List<CountryModel>> {
//        return appDatabase.countryDao().readAllCountries()
//    }

    override fun onComplete(result: ModelResponse.ModelData) {
        val models = result.data
        for (model in models) {
            addLog(model.obj)
        }
    }

    override fun onError(error: Throwable) {
        addLog("Something went wrong...")
        addLog(error.localizedMessage)
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseDisposable?.dispose()
        networkController.destroyConnection()
    }

    /** Bottomsheet callback functions **/
    override fun onSavePressed(itemsSelected: List<BottomSheetHelper.BottomsheetModel>) {
        addLog("You have selected the following:")
        for (item in itemsSelected) {
            addLog(item.name)
        }
    }

    override fun onSavePressed(itemSelected: BottomSheetHelper.BottomsheetModel) {
        addLog("You have selected ${itemSelected.name}")
    }

    override fun onCancelPressed() {
        addLog("User Cancelled")
    }

    /** Location callback functions **/
    override fun onLocationGathered(location: Location) {
        addLog("lat:${location.latitude} lon:${location.longitude}")
        addLog("Location has been collected")
    }

    override fun onLocationNotEnabled() {
        addLog("Location is not enabled")
    }

    override fun onFailedOut() {
        addLog("Location timed out... must be on the moon")
    }

}