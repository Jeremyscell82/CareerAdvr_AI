package com.lloydsbyte.careeradvr_ai.utilz

class ProfileValidator {
    companion object {

        fun validateAge(age: String): Boolean {
            return age.isNotEmpty() && age.toCharArray().size < 3
        }

        fun validateName(name: String): Boolean {
            return name.isNotEmpty() && name.trim().length > 2
        }
    }
}