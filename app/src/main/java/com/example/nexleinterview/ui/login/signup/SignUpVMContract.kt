package com.example.nexleinterview.ui.login.signup


interface SignUpVMContract {

    fun isValidFirstName(name: String): Boolean

    fun isValidLastName(name: String): Boolean

    fun isValidEmail(email: String): Boolean

    fun isValidPassword(password: String): Boolean

    fun passwordStrength(password: String)

    fun event()
}
