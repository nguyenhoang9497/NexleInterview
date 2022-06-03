package com.example.nexleinterview.ui.login

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.nexleinterview.R
import com.example.nexleinterview.ui.login.login.LoginFragment
import com.example.nexleinterview.ui.login.signup.SignUpFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    companion object {
        const val PASSWORD_PATTERN =
            "^(?=.*[!@#\$&*])(?=.*[0-9])(?=.*[a-z]).{6,18}\$"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        openLoginFragment()
    }

    private fun openLoginFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.loginContainer, LoginFragment.newInstance())
        }
    }

    internal fun openSignupFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.loginContainer, SignUpFragment.newInstance())
                .addToBackStack(SignUpFragment::class.java.name)
        }
    }

    private fun hideKeyboard(view: View) {
        view.clearFocus()
        (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
            view.windowToken,
            0
        )
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
