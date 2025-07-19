package com.example.mirmessager.feature.chat

import androidx.lifecycle.ViewModel
import com.android.identity.util.UUID
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.example.mirmessager.data.model.Message
import com.google.firebase.database.DatabaseError

//TODO "РАЗОБРАТЬСЯ В ПИЗДЕЦЕ"
@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    //разница между mutablestateflow mutableListOf и почему что-то с большой буквы а что-то нет и что-то требует сразу создания внутри например emptylist
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val message = _messages.asStateFlow()
    private val firebaseDatabase = Firebase.database


    fun listenForMessages(channelId: String) {
        firebaseDatabase.getReference("messages").child(channelId).orderByChild("createdAt")
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = mutableListOf<Message>()
                        snapshot.children.forEach { data ->
                            val message = data.getValue(Message::class.java)
                            message?.let {
                                list.add(it)
                            }

                        }
                        _messages.value = list
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

    }

    fun sendMessage(channelId: String, messageText: String) {
        val message = Message(
            firebaseDatabase.reference.push().key ?: UUID.randomUUID().toString(),
            Firebase.auth.currentUser?.uid ?: "",
            message = messageText,
            createdAt = System.currentTimeMillis(),
            senderNameString = Firebase.auth.currentUser?.displayName ?: "anonim",
            senderImage = null,
            imageUrl = null

        )
        firebaseDatabase.getReference("messages").child(channelId).push().setValue(message)


    }
}