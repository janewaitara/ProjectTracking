package com.mumbicodes.presentation.splash

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mumbicodes.domain.use_case.onBoarding.OnBoardingUseCases
import com.mumbicodes.presentation.util.navigation.Screens
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(
    private val onBoardingUseCases: OnBoardingUseCases
) : ViewModel() {

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading = _isLoading

    private val _startDestination: MutableState<String> = mutableStateOf(Screens.OnBoardingScreen.route)
    val startDestination = _startDestination

    init {
        viewModelScope.launch {
            onBoardingUseCases.readOnBoardingStateUseCase().collect { isOnBoarded ->
                if (isOnBoarded) {
                    _startDestination.value = Screens.AllProjectsScreens.route
                } else {
                    _startDestination.value = Screens.OnBoardingScreen.route
                }
            }
            _isLoading.value = false
        }
    }
}
