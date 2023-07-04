package com.example.nexleinterview.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.nexleinterview.R

abstract class BaseActivity<T : ViewBinding>(val inflate: (LayoutInflater) -> T) : AppCompatActivity() {
    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initListener()
    }

    abstract fun initData()

    abstract fun initListener()

    fun addFragment(fragment: Fragment, fragmentContainer: Int, isAddToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_out_right)
            .add(fragmentContainer, fragment)
        if (isAddToBackStack) {
            transaction.addToBackStack(fragment::class.java.name)
        } else {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    fun replaceFragment(fragment: Fragment, fragmentContainer: Int, isAddToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_out_right)
            .replace(fragmentContainer, fragment)
        if (isAddToBackStack) {
            transaction.addToBackStack(fragment::class.java.name)
        } else {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    fun popFragment() {
        var currentFragment = Fragment()
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.fragments.forEach {
                if (it.isVisible) {
                    currentFragment = it
                }
            }
            if (currentFragment.childFragmentManager.backStackEntryCount > 0) {
                currentFragment.childFragmentManager.popBackStackImmediate()
            } else {
                supportFragmentManager.popBackStackImmediate()
            }
        } else {
            finish()
        }
    }

    fun hideKeyboard(view: View) {
        view.clearFocus()
        (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
            view.windowToken,
            0
        )
    }
}