package com.lloydsbyte.careeradvr_ai.utilz

import com.lloydsbyte.careeradvr_ai.R

class CategoryHelper {
    companion object {
        val AMA: Int = 0
        val PERS: Int = 1
        val BUS: Int = 3
    }

    fun getIcon(cat: Int): Int {
        return when(cat) {
            AMA ->{
                R.drawable.ic_pro_financial
            }
            PERS ->{
                R.drawable.ic_pro_trainer
            }
            BUS ->{
                R.drawable.ic_pro_businessman
            }
            else -> {
                R.drawable.ic_pro_career
            }
        }
    }

}