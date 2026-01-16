package com.example.fitgod.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitgod.data.local.FitGodDatabase
import com.example.fitgod.data.repository.AuthRepository
import com.example.fitgod.data.repository.UserRepository
import com.example.fitgod.util.SessionManager
import com.example.fitgod.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState: StateFlow<UiState<Unit>> = _loginState

    private val _registerState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val registerState: StateFlow<UiState<Unit>> = _registerState

    fun login(username: String, password: String) {
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val result = authRepository.login(username, password)

                result.fold(
                    onSuccess = { user ->
                        // cache ke Room (optional)
                        userRepository.cacheUserFromApi(user)

                        // simpan sesi ke SharedPreferences
                        sessionManager.saveLogin(
                            userId = user.idUser,        // PERHATIKAN: userId, BUKAN idUser
                            username = user.username
                        )

                        _loginState.value = UiState.Success(Unit)
                    },
                    onFailure = { e ->
                        _loginState.value = UiState.Error(
                            e.message ?: "Username atau password salah"
                        )
                    }
                )
            } catch (e: Exception) {
                _loginState.value =
                    UiState.Error(e.message ?: "Terjadi kesalahan saat login")
            }
        }
    }

    fun register(username: String, password: String) {
        _registerState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val result = authRepository.register(username, password)

                result.fold(
                    onSuccess = { user ->
                        // cache ke Room (optional)
                        userRepository.cacheUserFromApi(user)

                        // langsung dianggap login
                        sessionManager.saveLogin(
                            userId = user.idUser,        // sama: userId
                            username = user.username
                        )

                        _registerState.value = UiState.Success(Unit)
                    },
                    onFailure = { e ->
                        _registerState.value = UiState.Error(
                            e.message ?: "Registrasi gagal"
                        )
                    }
                )
            } catch (e: Exception) {
                _registerState.value =
                    UiState.Error(e.message ?: "Terjadi kesalahan saat registrasi")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // kalau mau, bersihkan cache user
            userRepository.clearUsers()
        }
        sessionManager.clearSession()
        _loginState.value = UiState.Idle
        _registerState.value = UiState.Idle
    }

    fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()

    fun getUsername(): String? = sessionManager.getUsername()

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                        val db = FitGodDatabase.getInstance(context)
                        val userRepo = UserRepository(db.userDao())
                        val authRepo = AuthRepository()      // TANPA PARAMETER
                        val session = SessionManager(context)

                        @Suppress("UNCHECKED_CAST")
                        return AuthViewModel(authRepo, userRepo, session) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}