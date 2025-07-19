package com.example.mirmessager.data.model

data class Message(
    val id: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val senderNameString: String = "",
    val senderImage: String? = null,
    val imageUrl: String? = null,

    )


