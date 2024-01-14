package com.lloydsbyte.careeradvr_ai.image_gen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.lloydsbyte.careeradvr_ai.MainActivity
import com.lloydsbyte.careeradvr_ai.R
import com.lloydsbyte.careeradvr_ai.databinding.FragmentImageGenBinding
import com.lloydsbyte.careeradvr_ai.utilz.Gpt_Helper
import com.lloydsbyte.core.custombottomsheet.ErrorBottomsheet
import com.lloydsbyte.core.customdialog.CustomDialogs
import com.lloydsbyte.core.utilz.UtilzViewHelper
import com.lloydsbyte.network.NetworkClient
import com.lloydsbyte.network.NetworkConstants
import com.lloydsbyte.network.NetworkController
import com.lloydsbyte.network.interfaces.DallEInterface
import com.lloydsbyte.network.responses.DallEResponse
import timber.log.Timber

class ImageGenFragment: Fragment(), DallEInterface {

    lateinit var binding: FragmentImageGenBinding
    lateinit var viewModel: ImageGenViewModel

    val networkClient by lazy {
        NetworkClient.create(NetworkConstants.gptBaseUrl)
    }
    val networkController by lazy {
        NetworkController(networkClient)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageGenBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ImageGenViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            imgGenBackFab.setOnClickListener {
                findNavController().popBackStack()
            }
            imgGenShareFab.setOnClickListener {

            }
            imgGenDownloadFab.setOnClickListener {
                if (viewModel.response != null && !viewModel.response?.urlDataList.isNullOrEmpty()){
                    (requireActivity() as MainActivity).downloadImage(viewModel.response!!.urlDataList.first().imageUrl)
                }

            }
            imgGenSendFab.setOnClickListener {
                viewModel.phraseEntered = imgGenInputField.text.toString()
                if (viewModel.phraseEntered.isNotEmpty()){
                    generateImage()
                } else {
                    CustomDialogs.snackbar(root, "No phrase detected")
                }
            }
            imgGenInputField.apply {
                addTextChangedListener(object : TextWatcher {
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
                    }

                })
            }

            if (viewModel.response != null) {
                loadUI()
            } else {
                UtilzViewHelper.showSoftKeyboard(requireActivity(), binding.imgGenInputField)
            }

        }
    }

    private fun loadUI() {
        val url = viewModel.response?.urlDataList?.first()?.imageUrl
        Timber.d("JL_ url is $url")
        Glide.with(binding.root)
            .load(url)
            .placeholder(ResourcesCompat.getDrawable(resources, R.drawable.ic_gen_img, null))
            .into(binding.imgGenImage)
        CustomDialogs.snackbar(binding.root, "Loading image now")
    }

    private fun generateImage() {
        showLoader(true)
        UtilzViewHelper.hideSoftKeyboard(requireActivity(), binding.imgGenInputField)
        binding.imgGenInputField.clearFocus()
        networkController.dallERequest(this,
            Gpt_Helper().createDallEPayload(requireActivity(), viewModel.phraseEntered) )

    }

    fun showLoader(show: Boolean) {
        (requireActivity() as com.lloydsbyte.careeradvr_ai.MainActivity).showLoadingView(show)
    }

    override fun onCompleted(data: DallEResponse.DallEData) {
        viewModel.response = data
        showLoader(false)
        loadUI()
    }

    override fun onFailed(error: Throwable) {
        showLoader(false)
        CustomDialogs.snackbar(binding.root, "Something went wrong, let me show you")
        val bottomsheet = ErrorBottomsheet.createInstance("An error has occurred", error.message)
        bottomsheet.show(requireActivity().supportFragmentManager, bottomsheet.tag)
    }
}