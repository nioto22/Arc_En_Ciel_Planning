package com.aprouxdev.arcencielplanning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.aprouxdev.arcencielplanning.data.services.local.LoginState
import com.aprouxdev.arcencielplanning.databinding.ActivityLoginBinding
import com.aprouxdev.arcencielplanning.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        setupDataObservers()
        setupUiListeners()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupDataObservers() {
        lifecycleScope.launch {
            viewModel.isLoginEnable.collect {
                binding.loginButton.isEnabled = it
            }
        }
        lifecycleScope.launch {
            viewModel.hasError.collect {
                binding.loginError.isVisible = it
            }
        }
        lifecycleScope.launch {
            viewModel.loginState.collect {
                binding.loginLoader.isVisible = it == LoginState.Loading
                if (it is LoginState.Logged) {
                    viewModel.refreshAllData()
                }
            }
        }
    }

    private fun setupUiListeners() {
        with(binding) {
            loginNameEt.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    viewModel.setUserName(p0.toString())
                }
            })
            loginPasswordEt.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    viewModel.setPassword(p0.toString())
                }
            })
            loginButton.setOnClickListener {
                viewModel.login()
            }
        }
    }


}