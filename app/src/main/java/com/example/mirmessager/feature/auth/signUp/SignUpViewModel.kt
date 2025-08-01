package com.example.mirmessager.feature.auth.signUp

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

// почему сбда hilt а не сделать отдельный di файлик и почему раньше делал отдельный repository слой и отдельно viewModel а тут сразу
@HiltViewModel
class SignUpViewModel @Inject constructor() :ViewModel() {
    //разобрать синтаксис
    private val _state = MutableStateFlow<SignUpState>(SignUpState.Nothing)
    val state = _state.asStateFlow()


    //   не перемудренная ли тут логика? кажется много лишнего добавил и можно легче это сделать
    fun signUp(name: String, email: String, password: String) {
        _state.value = SignUpState.Loading
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.let { user ->
                        user.updateProfile(
                            UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()
                        )?.addOnCompleteListener {
                            _state.value = SignUpState.Success
                        }
                    } ?: run { _state.value = SignUpState.Error }
                } else {
                    _state.value = SignUpState.Error  // Теперь else на своем месте
                }
            }
    }
}

    sealed class SignUpState {
        object Nothing : SignUpState()
        object Loading : SignUpState()
        object Success : SignUpState()
        object Error : SignUpState()

    }
