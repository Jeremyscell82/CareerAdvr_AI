package com.lloydsbyte.careeradvr_ai.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.Slider
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.analytics.MixPanelConstants
import com.lloydsbyte.careeradvr_ai.analytics.MixPanelController
import com.lloydsbyte.careeradvr_ai.databinding.BottomsheetChatConfigBinding
import com.lloydsbyte.careeradvr_ai.utilz.Gpt_Helper
import com.lloydsbyte.careeradvr_ai.utilz.UserProfileHelper
import com.lloydsbyte.core.customdialog.CustomDialogs
import com.lloydsbyte.core.selection_bottomsheet.BottomSheetHelper
import com.lloydsbyte.core.selection_bottomsheet.BottomsheetSelectionFragment
import com.lloydsbyte.core.utilz.StoredPref

class ChatConfigBottomSheet: BottomSheetDialogFragment(),
    BottomSheetHelper.BottomsheetSelectionInterface {

    lateinit var binding: BottomsheetChatConfigBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetChatConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(true)
        binding.apply {
            dialogClose.setOnClickListener {
                dialog?.dismiss()
            }
            //Disable model selection if on Free account
            val freeStatus: Boolean = StoredPref(requireActivity()).getProfileStatus() == UserProfileHelper.statusFree
            settingsGptModelLayout.setOnClickListener {
                if (freeStatus) {
                    //Inform user they can not change the model on this current status, they must upgrade
                    CustomDialogs.toast(binding.root.context, "You must upgrade your membership")
                } else {
                    val bottomsheet = BottomsheetSelectionFragment.createInstance(
                        this@ChatConfigBottomSheet,
                        null,
                        BottomSheetHelper().createBottomSheetList(
                            resources.getStringArray(R.array.model_options).toList()
                        ),
                        true
                    )
                    bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
                }
            }
            settingsGptModelSubtitle.text = Gpt_Helper().convertModelForLegibility(StoredPref(requireActivity()).getChatModel(), resources.getStringArray(R.array.model_options).toList())


            //GPT Temp slider
            settingsTempHelpFab.setOnClickListener {
                //Todo display custom dialog with help info
                val bottomsheet = MoreInfoBottomSheet.createInstance(
                    resources.getString(R.string.chat_moreinfo_title),
                    resources.getString(R.string.chat_moreinfo_subtitle)
                )
                bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
            }
            settingsTempStatus.text = StoredPref(requireActivity()).chatTemp().toString()
            settingsTempSlider.value = StoredPref(requireActivity()).chatTemp().toFloat()
            settingsTempSlider.setLabelFormatter { value: Float ->
                return@setLabelFormatter "%.1f".format(value)
            }
            settingsTempSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: Slider) {}
                override fun onStopTrackingTouch(slider: Slider) {
                    val temp = "%.1f".format(slider.value)
                    settingsTempStatus.text = temp
                    StoredPref(requireActivity()).setChatTemp(temp.toDouble())
                }
            })
        }
    }

    override fun onSavePressed(itemsSelected: List<BottomSheetHelper.BottomsheetModel>) {
        TODO("Not yet implemented")
    }

    override fun onSavePressed(itemSelected: BottomSheetHelper.BottomsheetModel) {
//        val modelSelected = if (itemSelected.name == resources.getStringArray(R.array.model_options)[0]) NetworkConstants.gpt_4 else NetworkConstants.gpt_3
        val modelSelected = Gpt_Helper().convertSelectedModel(itemSelected.id)
        StoredPref(requireActivity()).setChatModel(modelSelected)
        binding.settingsGptModelSubtitle.text = Gpt_Helper().convertModelForLegibility(modelSelected, resources.getStringArray(R.array.model_options).toList())
        MixPanelController().reportUsageEvent(
            requireActivity(),
            MixPanelConstants.EVEN_SETTINGS,
            MixPanelConstants.VALUE_GPT_MODEL,
            itemSelected.name
        )
    }

    override fun onCancelPressed() {
        TODO("Not yet implemented")
    }

}