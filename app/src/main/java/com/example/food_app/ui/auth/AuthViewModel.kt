package com.example.food_app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app.data.Role
import com.example.food_app.data.User
import com.example.food_app.repository.UserPreferencesRepository
import com.example.food_app.repository.UserRepository
import com.example.food_app.util.PasswordHasher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthResult {
    data class Success(val role: Role) : AuthResult()
    data class Error(val message: String) : AuthResult()
    data class PasswordReset(val message: String) : AuthResult()
    object Loading : AuthResult()
    object Idle : AuthResult()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val prefsRepository: UserPreferencesRepository
) : ViewModel() {

    companion object {
        var lastRegisteredIdentifier: String? = "admin"
        var lastRegisteredPassword: String? = "admin"
    }

    private val _authResult = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val authResult = _authResult.asStateFlow()

    val savedCredentials = prefsRepository.lastLoggedInCredentials

    init {
        viewModelScope.launch {
            if (userRepository.findByUsername("admin") == null) {
                val adminUser = User(
                    username = "admin",
                    passwordHash = PasswordHasher.hash("admin"),
                    role = Role.ADMIN
                )
                userRepository.insert(adminUser)
            }
        }
    }

    fun login(identifier: String, password: String) {
        viewModelScope.launch {
            _authResult.value = AuthResult.Loading
            val user = userRepository.findByUsername(identifier)
            if (user == null) {
                _authResult.value = AuthResult.Error("Người dùng hoặc SĐT không tồn tại.")
            } else {
                if (user.passwordHash == PasswordHasher.hash(password)) {
                    prefsRepository.saveLastLoggedInCredentials(identifier, password)
                    _authResult.value = AuthResult.Success(user.role)
                } else {
                    _authResult.value = AuthResult.Error("Sai mật khẩu.")
                }
            }
        }
    }

    fun register(identifier: String, password: String) {
        viewModelScope.launch {
            _authResult.value = AuthResult.Loading
            if (userRepository.findByUsername(identifier) != null) {
                _authResult.value = AuthResult.Error("Tên người dùng hoặc SĐT đã tồn tại.")
                return@launch
            }
            try {
                val newUser = User(
                    username = identifier,
                    passwordHash = PasswordHasher.hash(password),
                    role = Role.CUSTOMER
                )
                userRepository.insert(newUser)
                lastRegisteredIdentifier = identifier
                lastRegisteredPassword = password
                _authResult.value = AuthResult.Success(Role.CUSTOMER)
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error("Đăng ký thất bại: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            prefsRepository.clearLastLoggedInCredentials()
        }
    }

    fun resetPassword(identifier: String) {
        viewModelScope.launch {
            _authResult.value = AuthResult.Loading
            val user = userRepository.findByUsername(identifier)
            if (user == null) {
                _authResult.value = AuthResult.Error("Người dùng hoặc SĐT không tồn tại.")
            } else {
                val newPassword = "123456"
                val updatedUser = user.copy(passwordHash = PasswordHasher.hash(newPassword))
                userRepository.update(updatedUser)
                _authResult.value = AuthResult.PasswordReset("Mật khẩu của '$identifier' đã được đặt lại thành '$newPassword'.")
            }
        }
    }

    fun resetResult() {
        _authResult.value = AuthResult.Idle
    }
}
