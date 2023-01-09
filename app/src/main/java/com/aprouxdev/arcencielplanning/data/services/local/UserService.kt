package com.aprouxdev.arcencielplanning.data.services.local


import comaprouxdevarcencielplanning.UserDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class LoginState {
    object Loading: LoginState()
    object None: LoginState()
    class Logged(val user: UserDb): LoginState()
}

internal class UserService {

    companion object {
        val instance = UserService()
    }

    private var _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Loading)
    val loginState: StateFlow<LoginState> = _loginState

    fun loadUser() {
        val userDb = database.userDbQueries.getAllUserDbs().executeAsOneOrNull()
        userDb?.let {
            _loginState.value = LoginState.Logged(it)
        } ?: kotlin.run {
            _loginState.value = LoginState.None
        }
    }

    fun insertUser(user: UserDb) {
        database.userDbQueries.insertOrReplace(user)
    }
}
