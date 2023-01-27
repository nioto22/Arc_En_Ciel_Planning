package com.aprouxdev.arcencielplanning.viewmodel

import androidx.lifecycle.ViewModel
import com.aprouxdev.arcencielplanning.data.enums.Teams
import com.aprouxdev.arcencielplanning.data.services.local.LoginState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import comaprouxdevarcencielplanning.UserDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class LoginViewModel: ViewModel() {

    private var mUserName: String? = null
    private var mPassword: String? = null

    private var _isLoginEnable = MutableStateFlow<Boolean>(false)
    val isLoginEnable: StateFlow<Boolean> get() = _isLoginEnable

    private var _loginState = MutableStateFlow<LoginState>(LoginState.None)
    val loginState: StateFlow<LoginState> get() = _loginState

    private var _hasError = MutableStateFlow<Boolean>(false)
    val hasError: StateFlow<Boolean> get() = _hasError

    fun setUserName(name: String) {
        mUserName = name
        setButtonEnabled()
    }

    fun setPassword(password: String) {
        mPassword = password
        setButtonEnabled()
    }

    private fun setButtonEnabled() {
        _isLoginEnable.value = !mUserName.isNullOrEmpty() && !mPassword.isNullOrEmpty()
    }

    fun login() {
        if (mUserName.isNullOrEmpty() || mPassword.isNullOrEmpty()) return
        _loginState.value = LoginState.Loading
        if (mUserName == "Antoine" && mPassword == "1234") {
            val userDb = UserDb(id= "1", name = "Antoine", isAdmin= true, imageUrl = "https://product-image.juniqe-production.juniqe.com/media/catalog/product/seo-cache/x800/642/69/642-69-401P/Fashion-Llama-Paul-Fuentes-Impression-sur-verre.jpg", teams = listOf(Teams.Shop.getName(), Teams.Toys.getName()))
            _loginState.value = LoginState.Logged(userDb)
        }
        /*Firebase.firestore.collection("user")
            .whereEqualTo("name", mUserName)
            .whereEqualTo("pass", mPassword)
            .get()
            .addOnSuccessListener {
                val user: UserDb? = it.documents.firstOrNull()?.toObject()
                if (user == null) {
                    _loginState.value = LoginState.None
                    _hasError.value = true
                } else {
                    _loginState.value = LoginState.Logged(user)
                }
            }
            .addOnFailureListener {
                _loginState.value = LoginState.None
                _hasError.value = true
            }

         */
    }

    fun refreshAllData() {

    }


}