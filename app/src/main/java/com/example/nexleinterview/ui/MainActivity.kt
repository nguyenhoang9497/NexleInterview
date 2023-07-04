package com.example.nexleinterview.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.example.nexleinterview.R
import com.example.nexleinterview.databinding.ActivityMainBinding
import com.example.nexleinterview.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels()

    private val adapter: MainAdapter by lazy {
        MainAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override suspend fun initState() {
        viewModel.stateFLow.collect{
            when(it) {
                MainViewModel.MainState.OK -> {
                    adapter.setData(mutableListOf("Text 1", "Text 1", "Text 1", "Text 1", "Text 1", "Text 1", "Text 1", ))
                }
            }
        }
    }

    override fun initListener() {
        binding.recyclerView.adapter = adapter
        adapter.onItemClick = { int, string ->
            Log.d("AAAA", "initListener: $int, $string")
        }
    }
}
