package com.example.mirmessager.feature.auth.signIn

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

// почему сбда hilt а не сделать отдельный di файлик и почему раньше делал отдельный repository слой и отдельно viewModel а тут сразу
@HiltViewModel
class SignInViewModel @Inject constructor() :ViewModel(){
    //разобрать синтаксис
private val _state = MutableStateFlow<SignInState>(SignInState.Nothing)
    val state =_state.asStateFlow()

    // что такое онкомплит это метод локальный файрбейз же?  а зачем в конце функции все равно сделали success
    fun signIn(email:String, password: String){
        _state.value = SignInState.Loading
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
//                что тут добавил что за result user let для чего эт очто мы возврвщаем?
                if (task.isSuccessful){
                    //это проверка user на существование?  почитал документацию наверное избыточно
                    task.result.user?.let{
                        _state.value = SignInState.Success
                        return@addOnCompleteListener
                    }
                  _state.value = SignInState.Error
                } else {
                    _state.value = SignInState.Error
                }
            }
    }
}

sealed class SignInState{
    object Nothing : SignInState()
    object Loading : SignInState()
    object Success : SignInState()
    object Error : SignInState()

}