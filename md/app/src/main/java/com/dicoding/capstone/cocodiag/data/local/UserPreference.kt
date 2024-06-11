package com.dicoding.capstone.cocodiag.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dicoding.capstone.cocodiag.data.local.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun saveUser(user: UserModel) {
        dataStore.edit { pref ->
            pref[ID_KEY] = user.id
            pref[NAME_KEY] = user.name
            pref[EMAIL_KEY] = user.email
            pref[PASSWORD_KEY] = user.password
            pref[IMAGE_KEY] = user.imageProfile ?: ""
            pref[TOKEN_KEY] = user.token ?: ""
            pref[STATE_KEY] = user.isSigned
        }
    }

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { pref ->
            UserModel(
                pref[ID_KEY] ?: "",
                pref[NAME_KEY] ?: "",
                pref[EMAIL_KEY] ?: "",
                pref[PASSWORD_KEY] ?: "",
                pref[IMAGE_KEY] ?: "",
                pref[TOKEN_KEY] ?: "",
                pref[STATE_KEY] ?: false
            )
        }
    }

    fun getUserId(): Flow<String?> {
        return dataStore.data.map { pref ->
            pref[ID_KEY] ?: ""
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { pref ->
            pref[TOKEN_KEY] ?: ""
        }
    }

    fun getPassword():Flow<String?>{
        return dataStore.data.map { pref ->
            pref[PASSWORD_KEY]?:""
        }
    }

    fun getEmail():Flow<String?>{
        return dataStore.data.map { pref ->
            pref[EMAIL_KEY]?:""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val ID_KEY = stringPreferencesKey("user_id")
        private val NAME_KEY = stringPreferencesKey("user_name")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
        private val PASSWORD_KEY = stringPreferencesKey("user_password")
        private val IMAGE_KEY = stringPreferencesKey("user_image")
        private val TOKEN_KEY = stringPreferencesKey("user_token")
        private val STATE_KEY = booleanPreferencesKey("user_state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}