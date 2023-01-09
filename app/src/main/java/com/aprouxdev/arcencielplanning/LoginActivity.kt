package com.aprouxdev.arcencielplanning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aprouxdev.arcencielplanning.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        setupDataObservers()
    }

    private fun setupDataObservers() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}