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
            pref[NAME_KEY] = user.name
            pref[EMAIL_KEY] = user.email
            pref[PASSWORD_KEY] = user.password
            pref[STATE_KEY] = user.isSigned
        }
    }

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { pref ->
            UserModel(
                pref[NAME_KEY] ?: "",
                pref[EMAIL_KEY] ?: "",
                pref[PASSWORD_KEY] ?: "",
                pref[STATE_KEY] ?: false
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("user_name")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
        private val PASSWORD_KEY = stringPreferencesKey("user_password")
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