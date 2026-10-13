package com.lastwave.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastwave.app.data.local.UserProfilePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    val profilePrefs: UserProfilePreferences,
) : ViewModel() {
    fun saveProfile(displayName: String, avatarIndex: Int) {
        viewModelScope.launch {
            profilePrefs.saveProfile(displayName, avatarIndex)
        }
    }
}
