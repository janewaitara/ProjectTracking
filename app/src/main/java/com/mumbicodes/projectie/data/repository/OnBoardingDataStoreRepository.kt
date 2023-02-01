package com.mumbicodes.projectie.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val ON_BOARDING_PREF_NAME = "on_boarding_pref"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = ON_BOARDING_PREF_NAME
)

class OnBoardingDataStoreRepository(
    context: Context,
) {
    private companion object {
        val IS_ONBOARDED = booleanPreferencesKey("is_onboarded")
    }

    private val datastore = context.dataStore

    suspend fun saveOnBoardingState(isOnBoarded: Boolean) {
        datastore.edit { preferences ->
            preferences[IS_ONBOARDED] = isOnBoarded
        }
    }

    fun readOnBoardingState(): Flow<Boolean> {
        return datastore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val onBoardingState = preferences[IS_ONBOARDED] ?: false
                onBoardingState
            }
    }
}
