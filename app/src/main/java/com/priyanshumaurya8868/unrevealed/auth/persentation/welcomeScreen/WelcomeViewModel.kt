package com.priyanshumaurya8868.unrevealed.auth.persentation.welcomeScreen

import androidx.lifecycle.ViewModel
import com.priyanshumaurya8868.unrevealed.core.ThemeSwitcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(val themeSwitcher: ThemeSwitcher): ViewModel() {
}