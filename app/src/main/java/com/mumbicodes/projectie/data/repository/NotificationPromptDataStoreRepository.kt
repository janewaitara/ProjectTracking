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

private const val NOTIFICATION_PROMPT_PREF_NAME = "notification_prompt_pref"
val Context.notificationDataStore: DataStore<Preferences> by preferencesDataStore(
    name = NOTIFICATION_PROMPT_PREF_NAME
)

class NotificationPromptDataStoreRepository(
    context: Context,
) {
    private companion object {
        val IS_PROMPTED = booleanPreferencesKey("is_prompted")
    }

    private val datastore = context.notificationDataStore

    suspend fun saveNotificationPromptState(isPrompted: Boolean) {
        datastore.edit { preferences ->
            preferences[IS_PROMPTED] = isPrompted
        }
    }

    fun readNotificationPromptState(): Flow<Boolean> {
        return datastore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val notificationPromptState = preferences[IS_PROMPTED] ?: false
                notificationPromptState
            }
    }
}
