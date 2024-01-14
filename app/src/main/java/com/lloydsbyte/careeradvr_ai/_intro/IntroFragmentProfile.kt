package com.lloydsbyte.careeradvr_ai._intro

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lloydsbyte.careeradvr_ai.databinding.FragmentIntoProfileBinding
import com.lloydsbyte.careeradvr_ai.utilz.ProfileValidator
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.core.utilz.UtilzViewHelper

class IntroFragmentProfile : Fragment() {

    lateinit var binding: FragmentIntoProfileBinding
    var validName: Boolean = false
    var validAge: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntoProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            introNextFab.setOnClickListener {
                if (validAge && validName) {
                    //Store values and move on to next screen
                    val prefs = StoredPref(requireActivity())
                    prefs.setUserAge(profileAgeField.text.toString())
                    prefs.setUserName(profileNameField.text.toString())
                    UtilzViewHelper.hideSoftKeyboard(requireActivity(), binding.profileAgeField)
//                    findNavController().navigate(R.id.action_profile_to_signin)
                    (requireActivity() as IntroSectionActivity).initiateApp()
                }
            }
            UtilzViewHelper.showSoftKeyboard(requireActivity(), profileNameField)
            profileNameField.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {
                    validName = ProfileValidator.validateName(s.toString())
                    showNextFab()
                }

            })
            profileAgeField.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {
                    validAge = ProfileValidator.validateAge(s.toString())
                    showNextFab()
                }

            })
        }
    }

    private fun showNextFab() {
        binding.introNextFab.visibility = if (validAge && validName) View.VISIBLE else View.GONE
    }
}