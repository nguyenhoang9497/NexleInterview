package com.example.nexleinterview.ui.login

import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import com.example.nexleinterview.R
import com.example.nexleinterview.databinding.ActivityLoginBinding
import com.example.nexleinterview.ui.base.BaseActivity
import com.example.nexleinterview.ui.login.login.LoginFragment
import com.example.nexleinterview.ui.login.signup.SignUpFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    companion object {
        const val PASSWORD_PATTERN =
            "^(?=.*[!@#\$&*])(?=.*[0-9])(?=.*[a-z]).{6,18}\$"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openLoginFragment()
    }

    override suspend fun initState() {

    }

    override fun initListener() {

    }

    private fun openLoginFragment() {
        replaceFragment(LoginFragment.newInstance(), R.id.loginContainer)
    }

    internal fun openSignupFragment() {
        addFragment(SignUpFragment.newInstance(), R.id.loginContainer)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        val location = IntArray(2)
        when (ev.action) {
            MotionEvent.ACTION_DOWN ->
                if (view is EditText) {
                    if (currentFocus !is EditText) {
                        currentFocus?.clearFocus()
                        hideKeyboard(view)
                    } else {
                        currentFocus?.let {
                            it.getLocationOnScreen(location)
                            val x = ev.rawX + it.left - location[0]
                            val y = ev.rawY + it.top - location[1]
                            if (x < it.left || x >= it.right || y < it.top || y > it.bottom) {
                                hideKeyboard(it)
                            }
                        }
                    }
                }
        }
        return super.dispatchTouchEvent(ev)
    }
}
