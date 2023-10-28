package com.example.cssenew

import com.google.firebase.firestore.DocumentId

data class offers(

    var userId: String = "",
    @DocumentId val id: String? = null,
    val title: String = "",
    val sellername: String = "",
    val description: String = "",
    val category: String = "",

)
