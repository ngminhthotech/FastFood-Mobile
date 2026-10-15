package com.example.food_app.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

data class UserCredentials(val identifier: String, val password: String)

@Singleton
class UserPreferencesRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private object Keys {
        val LAST_IDENTIFIER = stringPreferencesKey("last_identifier")
        val LAST_PASSWORD = stringPreferencesKey("last_password")
    }

    val lastLoggedInCredentials: Flow<UserCredentials?> = context.dataStore.data.map { preferences ->
        val identifier = preferences[Keys.LAST_IDENTIFIER]
        val password = preferences[Keys.LAST_PASSWORD]
        if (identifier != null && password != null) {
            UserCredentials(identifier, password)
        } else {
            null
        }
    }

    suspend fun saveLastLoggedInCredentials(identifier: String, password: String) {
        context.dataStore.edit {
            preferences ->
            preferences[Keys.LAST_IDENTIFIER] = identifier
            preferences[Keys.LAST_PASSWORD] = password
        }
    }

    // Hàm mới để xóa thông tin đăng nhập đã lưu
    suspend fun clearLastLoggedInCredentials() {
        context.dataStore.edit {
            preferences ->
            preferences.clear()
        }
    }
}
