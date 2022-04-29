package com.priyanshumaurya8868.unrevealed.auth.persentation.genderSelection

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthConstants.VAL_FEMALE
import com.priyanshumaurya8868.unrevealed.auth.persentation.core.AuthConstants.VAL_MALE
import com.priyanshumaurya8868.unrevealed.core.Constants.ARG_USERNAME
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GenderSelectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isMale = mutableStateOf(false)
    val isMale: State<Boolean> = _isMale

    var username: String
    var password: String
    var gender: String = if (isMale.value) VAL_MALE
    else VAL_FEMALE

    init {
        savedStateHandle.apply {
            username = get<String>(ARG_USERNAME).toString()
            password = get<String>(ARG_USERNAME).toString()
        }

//        Log.d("omegaRanger/gsV", "Got username : $username,password : $password")

    }

    fun onGenderChange(isMale: Boolean) {
        _isMale.value = isMale
        gender = if (isMale) "Male"
        else "Female"
    }

}