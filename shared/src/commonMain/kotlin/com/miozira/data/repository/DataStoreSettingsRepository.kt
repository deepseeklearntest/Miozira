package com.miozira.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.miozira.content.LanguageId
import com.miozira.core.result.AppError
import com.miozira.core.result.AppResult
import kotlinx.coroutines.flow.first

interface SettingsRepository {
    suspend fun isSetupComplete(): AppResult<Boolean>
    suspend fun setSetupComplete(value: Boolean): AppResult<Unit>
    suspend fun getStartingLanguage(): AppResult<LanguageId?>
    suspend fun setStartingLanguage(languageId: LanguageId?): AppResult<Unit>
    suspend fun isSpeechAttemptDetectionEnabled(): AppResult<Boolean>
    suspend fun setSpeechAttemptDetectionEnabled(value: Boolean): AppResult<Unit>
}

class DataStoreSettingsRepository(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {
    override suspend fun isSetupComplete(): AppResult<Boolean> = read { preferences ->
        preferences[setupCompleteKey] ?: false
    }

    override suspend fun setSetupComplete(value: Boolean): AppResult<Unit> = write {
        it[setupCompleteKey] = value
    }

    override suspend fun getStartingLanguage(): AppResult<LanguageId?> = read { preferences ->
        preferences[startingLanguageKey]?.let(::LanguageId)
    }

    override suspend fun setStartingLanguage(languageId: LanguageId?): AppResult<Unit> = write { preferences ->
        if (languageId == null) preferences.remove(startingLanguageKey)
        else preferences[startingLanguageKey] = languageId.value
    }

    override suspend fun isSpeechAttemptDetectionEnabled(): AppResult<Boolean> = read { preferences ->
        preferences[speechAttemptDetectionEnabledKey] ?: false
    }

    override suspend fun setSpeechAttemptDetectionEnabled(value: Boolean): AppResult<Unit> = write {
        it[speechAttemptDetectionEnabledKey] = value
    }

    private suspend fun <T> read(read: (Preferences) -> T): AppResult<T> = try {
        AppResult.Success(read(dataStore.data.first()))
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not read local settings"))
    }

    private suspend fun write(write: (MutablePreferences) -> Unit): AppResult<Unit> = try {
        dataStore.edit(write)
        AppResult.Success(Unit)
    } catch (error: Exception) {
        AppResult.Failure(AppError.Unexpected("Could not save local settings"))
    }

    private companion object {
        val setupCompleteKey = booleanPreferencesKey("setup_complete")
        val startingLanguageKey = stringPreferencesKey("starting_language")
        // This preference is an adult opt-in; Android permission remains OS-owned.
        val speechAttemptDetectionEnabledKey = booleanPreferencesKey("speech_attempt_detection_enabled")
    }
}
