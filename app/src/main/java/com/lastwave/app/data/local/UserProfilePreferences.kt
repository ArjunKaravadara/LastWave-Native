package com.lastwave.app.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AKWave user profile: display name + avatar choice, collected once during
 * onboarding (ProfileSetupScreen) and editable later from Settings.
 */
data class UserProfileData(
    val displayName: String = "",
    val avatarIndex: Int = 0,
    val setupComplete: Boolean = false,
    val isLoaded: Boolean = false,
)

@Singleton
class UserProfilePreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val externalScope: CoroutineScope,
) {
    private object Keys {
        val DISPLAY_NAME = stringPreferencesKey("akwave_display_name")
        val AVATAR_INDEX = intPreferencesKey("akwave_avatar_index")
        val SETUP_COMPLETE = booleanPreferencesKey("akwave_profile_setup_complete")
    }

    val profile: StateFlow<UserProfileData> = dataStore.data
        .recoverPreferences("UserProfilePreferences")
        .map { p ->
            UserProfileData(
                displayName = p.readSafely(Keys.DISPLAY_NAME) ?: "",
                avatarIndex = p.readSafely(Keys.AVATAR_INDEX) ?: 0,
                setupComplete = p.readSafely(Keys.SETUP_COMPLETE) ?: false,
                isLoaded = true,
            )
        }.stateIn(
            scope = externalScope,
            started = SharingStarted.Eagerly,
            initialValue = UserProfileData(),
        )

    suspend fun saveProfile(displayName: String, avatarIndex: Int) {
        dataStore.edit { p ->
            p[Keys.DISPLAY_NAME] = displayName.trim()
            p[Keys.AVATAR_INDEX] = avatarIndex
            p[Keys.SETUP_COMPLETE] = true
        }
    }

    suspend fun updateName(displayName: String) {
        dataStore.edit { p -> p[Keys.DISPLAY_NAME] = displayName.trim() }
    }

    suspend fun updateAvatar(avatarIndex: Int) {
        dataStore.edit { p -> p[Keys.AVATAR_INDEX] = avatarIndex }
    }
}
