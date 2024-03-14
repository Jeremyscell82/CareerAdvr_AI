package com.lloydsbyte.careeradvr_ai.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.lloydsbyte.careeradvr_ai.MainActivity
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.databinding.FragmentSettingsBinding
import com.lloydsbyte.careeradvr_ai.utilz.UserProfileHelper
import com.lloydsbyte.core.customdialog.CustomDialogs
import com.lloydsbyte.core.custombottomsheet.BottomsheetEditFieldFragment
import com.lloydsbyte.core.search_bottomsheet.BottomsheetSearchInterface
import com.lloydsbyte.core.selection_bottomsheet.BottomSheetHelper
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.core.utilz.Utilz
import com.lloydsbyte.core.utilz.UtilzSendItHelper

/**
 * This is a pre designed settings fragment, comes wired with goodies
 */
class SettingsFragment : Fragment(), BottomSheetHelper.BottomsheetSelectionInterface,
    BottomsheetSearchInterface {

    lateinit var binding: FragmentSettingsBinding
    var storedPrefId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            Glide.with(root)
                .load(ResourcesCompat.getDrawable(resources, R.drawable.ic_chatbot, null))
                .into(settingsImg)
            settingsVersion.text = resources.getString(
                R.string.settings_version,
                Utilz.getVersionName(requireActivity()),
                Utilz.getBuildNumber(requireActivity())
            )
            settingsBackFab.setOnClickListener {
                findNavController().popBackStack()
            }
            //Upgrade your experience
            settingsUpgradeRmAdsLayout.setOnClickListener {
                CustomDialogs.snackbar(binding.root, "Upgrade option coming soon")
            }
            settingsUpgradeSubscribeLayout.setOnClickListener {
                CustomDialogs.snackbar(binding.root, "Subscribe option coming soon")
            }

            //Profile
            settingsProfileNameSubtitle.text = resources.getString(
                R.string.settings_profile_name_subtitle,
                StoredPref(requireActivity()).getUserName()
            )
            settingsProfileNameLayout.setOnClickListener {
                val bottomsheet = BottomsheetEditFieldFragment.createInstance(
                    "Edit name",
                    StoredPref(requireActivity()).getUserName(),
                    false,
                    this@SettingsFragment
                )
                storedPrefId = StoredPref(requireActivity()).PROFILE_NAME
                bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
            }


            settingsProfileAgeSubtitle.text = resources.getString(
                R.string.settings_profile_age_subtitle,
                StoredPref(requireActivity()).getUserAge()
            )
            settingsProfileAgeLayout.setOnClickListener {
                val bottomsheet = BottomsheetEditFieldFragment.createInstance(
                    "Edit age",
                    StoredPref(requireActivity()).getUserAge(),
                    true,
                    this@SettingsFragment
                )
                storedPrefId = StoredPref(requireActivity()).PROFILE_AGE
                bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
            }

            //App Settings
            settingsRateLayout.setOnClickListener {
                Utilz.rateApp(requireActivity())
            }
            settingsShareLayout.setOnClickListener {
                UtilzSendItHelper().shareApp(requireActivity())
            }
            settingsAboutLayout.setOnClickListener {
                val bottomsheet = SettingsAboutBottomsheet()
                bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
            }
            settingsContactLayout.setOnClickListener {
                UtilzSendItHelper().contactDeveloper(
                    requireActivity(),
                    Utilz.getVersionName(requireActivity())
                )
            }
            settingsContactLayout.setOnLongClickListener {
                UtilzSendItHelper().contactDeveloperWithUID(
                    requireActivity(),
                    Utilz.getVersionName(requireActivity()),
                    FirebaseAuth.getInstance().currentUser?.uid ?: "NA"
                )
                true
            }

            /**
             *  Debug Panel
             */
            settingsDebugCardview.visibility =
                if (Utilz.isInDebugMode(requireActivity())) View.VISIBLE else View.GONE

            settingsDebugTokensSubtitle.text =
                StoredPref(requireActivity()).getTokenCount().toString()

            settingsDebugResetLayout.setOnClickListener {
                UserProfileHelper.updateUsageLimit(0)
                UserProfileHelper.updateDateLimit(0L)
            }
            settingsDebugMiscLayout.setOnClickListener {
//                    UserProfileHelper.updateUserStatus("FREE")
//                    UserProfileHelper.updateDateLimit(UtilzDateHelper(UtilzDateHelper.DF_TIMEDATE).buildMillisDate())
//                    UserProfileHelper.updateUsageLimit()
                (requireActivity() as MainActivity).fireOffAd()
            }


            //Tokens Total
//            settingsTokensSubtitle.text = UtilzTokenHelper.calculateTotals(requireActivity()).toString()


            /** ACCOUNT CARDVIEW **/
//            settingsSigninLayout.setOnClickListener {
//                //Todo Sign in logic here
//                it.visibility = View.GONE
//            }
//            settingsPurchaseLayout.setOnClickListener {
//                //Send to purhcase screen
//                it.visibility = View.GONE
//            }


            //APP CARDVIEW
//            settingsRateLayout.setOnClickListener {
//                Utilz.rateApp(requireActivity())
//            }
//            settingsShareLayout.setOnClickListener {
//                UtilzSendItHelper().shareApp(requireActivity())
//            }
//            settingsContactLayout.setOnClickListener {
//                UtilzSendItHelper().contactDeveloper(requireActivity(), Utilz.getVersionName(requireActivity()))
//            }
//            settingsAboutLayout.setOnClickListener {
//                val bottomsheet = SettingsAboutBottomsheet()
//                bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
//                //Or
////                val bottomsheet = SettingsAboutAppBottomsheet()
////                bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
//            }


            //DEBUG PANEL
//            settingsDebugCardview.apply {
//                visibility = View.VISIBLE//if (Utilz.isInDebugMode())View.VISIBLE else View.GONE
//                settingsDebugLayout.setOnClickListener {
//                    //Report bug
//                    UtilzSendItHelper().reportBug(
//                        requireActivity(),
//                        resources.getString(R.string.settings_version, Utilz.getVersionName(requireActivity()), Utilz.getBuildNumber(requireActivity()))
//                    )
//                }
//                settingsTokensAddLayout.setOnClickListener {
//
//                }
//                settingsTokensClearLayout.setOnClickListener {
//
//                }
//                settingsClearAppLayout.setOnClickListener {
//
//                }
//            }

        }
    }

    //Multi Select Call back
    override fun onSavePressed(itemsSelected: List<BottomSheetHelper.BottomsheetModel>) {}

    override fun onSavePressed(itemSelected: BottomSheetHelper.BottomsheetModel) {
        UserProfileHelper.updateUserStatus(itemSelected.name)
        StoredPref(requireActivity()).setProfileStatus(itemSelected.name)
    }

    override fun onCancelPressed() {

    }

    override fun onPhraseEntered(query: String) {
        StoredPref(requireActivity()).writeString(storedPrefId, query)
        if (storedPrefId == StoredPref(requireActivity()).PROFILE_NAME) {
            binding.settingsProfileNameSubtitle.text =
                resources.getString(R.string.settings_profile_name_subtitle, query)
        } else if (storedPrefId == StoredPref(requireActivity()).PROFILE_AGE) {
            binding.settingsProfileAgeSubtitle.text =
                resources.getString(R.string.settings_profile_age_subtitle, query)
        }
        storedPrefId = ""
    }

    override fun onCanceled() {
        //Not used
    }


}