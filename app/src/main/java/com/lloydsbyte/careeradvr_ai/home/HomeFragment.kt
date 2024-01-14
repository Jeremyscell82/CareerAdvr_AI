package com.lloydsbyte.careeradvr_ai.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.lloydsbyte.careeradvr_ai.MainActivity
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.analytics.MixPanelConstants
import com.lloydsbyte.careeradvr_ai.analytics.MixPanelController
import com.lloydsbyte.careeradvr_ai.chat.ChatFragment
import com.lloydsbyte.careeradvr_ai.databinding.FragmentHomeBinding
import com.lloydsbyte.careeradvr_ai.proSpecific.ProSpecificFragment
import com.lloydsbyte.careeradvr_ai.utilz.CategoryHelper
import com.lloydsbyte.careeradvr_ai.utilz.Gpt_Helper
import com.lloydsbyte.careeradvr_ai.utilz.UserProfileHelper
import com.lloydsbyte.core.ErrorController
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.core.utilz.UtilzDateHelper

/**
 * This fragment will hold the profile screen, basically the portfolio screen
 */
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            homeSettingsFab.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
            }

            homeWelcomeText.text = resources.getString(
                R.string.home_welcome_text,
                StoredPref(requireActivity()).getUserName()
            )
            homeStartAmaChatCardview.setOnClickListener {
                MixPanelController().reportUsageEvent(
                    requireActivity(),
                    MixPanelConstants.EVENT_CHAT,
                    MixPanelConstants.VALUE_AMA,
                    StoredPref(requireActivity()).getChatModel()
                )
                val bundle = Bundle()
                val defaultPrompt =  Gpt_Helper().promptCreator(requireActivity(), (requireActivity() as MainActivity).getDefaultPrompt(), null, null)
                bundle.putString(ChatFragment.PROMPT_KEY,defaultPrompt)
                bundle.putInt(ChatFragment.PROMPT_CAT, CategoryHelper.AMA)
                findNavController().navigate(R.id.action_homeFragment_to_chatFragment, bundle)
            }

            homeViewSavedChatsCardview.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
            }

            homeProPersonalFab.setOnClickListener {
                val bundle = Bundle()
                bundle.putBoolean(ProSpecificFragment.PERSONAL_BOOL_KEY, true)
                findNavController().navigate(R.id.action_homeFragment_to_proFragment, bundle)
            }
            homeProBusinessFab.setOnClickListener {
                val bundle = Bundle()
                bundle.putBoolean(ProSpecificFragment.PERSONAL_BOOL_KEY, false)
                findNavController().navigate(R.id.action_homeFragment_to_proFragment, bundle)
            }

            //Update User Feeds
            UserProfileHelper.createProfileReference(UserProfileHelper.endpointStatus).addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val status: String = snapshot.value.toString()
                        StoredPref(requireActivity()).setProfileStatus(status)
                    } else {
                        UserProfileHelper.updateUserStatus(UserProfileHelper.statusFree)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    ErrorController.logError("DB status cancelled: ${error.toException()}")
                }

            })

            UserProfileHelper.createProfileReference(UserProfileHelper.endpointDateLimit).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val date: String = snapshot.value.toString()
                        StoredPref(requireActivity()).setUsageDate(date.toLong())
                    } else {
                        UserProfileHelper.updateDateLimit(UtilzDateHelper(UtilzDateHelper.DF_CAL).buildMillisDate())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    ErrorController.logError("DB Date cancelled: ${error.toException()}")
                }

            })

            UserProfileHelper.createProfileReference(UserProfileHelper.endpointUsageLimit).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val limit: String = snapshot.value.toString()
                        StoredPref(requireActivity()).setUsageLimit(limit.toInt())
                    } else {
                        UserProfileHelper.updateUsageLimit(0)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    ErrorController.logError("DB usage cancelled: ${error.toException()}")
                }

            })
        }
    }

}