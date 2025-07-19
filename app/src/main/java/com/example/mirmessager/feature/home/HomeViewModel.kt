package com.example.mirmessager.feature.home

import androidx.lifecycle.ViewModel
import com.example.mirmessager.data.model.Channel
import com.google.firebase.Firebase
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    private val firebaseDatabase = Firebase.database
    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    //заяем asStateFlow
    val channels = _channels.asStateFlow()
    init {
        getChannels()
    }
 fun addChannel(name:String){
    //что значит достали тут ключ откуда он взялся это новый? и потом по этому ключу добавили значение?
    val key = firebaseDatabase.getReference("channel").push().key
    firebaseDatabase.getReference("channel").child(key!!).setValue(name).addOnSuccessListener {
        getChannels()
    }
}
    //разобрать
    private fun getChannels() {
        firebaseDatabase.getReference("channel").get().addOnSuccessListener {
            // как создать mutableList ()
            val list = mutableListOf<Channel>()
            it.children.forEach { data ->
                val channel = Channel(data.key!!, data.value.toString())
                list.add(channel)

            }
    _channels.value = list
        }
    }
}