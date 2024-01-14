package com.lloydsbyte.careeradvr_ai.image_gen

import androidx.lifecycle.ViewModel
import com.lloydsbyte.network.responses.DallEResponse

class ImageGenViewModel: ViewModel() {

    var response: DallEResponse.DallEData? = null
    var phraseEntered: String = ""

}