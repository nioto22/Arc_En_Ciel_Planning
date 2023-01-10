package com.aprouxdev.arcencielplanning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.aprouxdev.arcencielplanning.data.services.local.database
import com.aprouxdev.arcencielplanning.databinding.ActivitySetupBinding
import com.aprouxdev.arcencielplanning.viewmodel.SetupState
import com.aprouxdev.arcencielplanning.viewmodel.SetupViewModel
import comaprouxdevarcencielplanning.UserDb
import kotlinx.coroutines.launch

class SetupActivity : AppCompatActivity() {

    private var _binding: ActivitySetupBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var viewModel: SetupViewModel

    private var user: UserDb? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySetupBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SetupViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        setupDataObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupDataObservers() {
        lifecycleScope.launch {
            viewModel.setupState.collect {
                binding.splashLoader.isVisible = it != SetupState.NoUser
                when(it) {
                    is SetupState.Logged -> {
                        this@SetupActivity.user = it.user
                        refreshAllData()
                    }
                    SetupState.NoUser -> goToLoginActivity()
                    SetupState.Loading -> Unit
                }
            }
        }
        lifecycleScope.launch {
            viewModel.isLoadingComplete.collect { isComplete ->
                if (isComplete) {
                    goToMainActivity()
                }
            }
        }
    }

    private fun refreshAllData() {
        viewModel.refreshAllData()
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this@SetupActivity, LoginActivity::class.java))
    }

    private fun goToMainActivity() {
        startActivity(Intent(this@SetupActivity, MainActivity::class.java))
    }

}