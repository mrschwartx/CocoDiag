package com.dicoding.capstone.cocodiag.data.dummy

import java.util.Date

data class ForumStatus(
    val name: String,
    val email: String,
    val profileImage: String,
    val textContent: String,
    val imageContent: String? = null,
    val likeCount: Int,
    val commentCount: Int,
    val createdAt: Date
)

val dummyForumStatus = listOf(
    ForumStatus(
        "John Doe",
        "johndoe@gmail",
        "https://randomuser.me/api/portraits/med/men/75.jpg",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
        "https://upload.wikimedia.org/wikipedia/commons/1/1d/Getting_the_coconut_the_Timorese_way.jpg",
        10,
        2,
        Date()
    ),
    ForumStatus(
        "John Doe 2",
        "johndoe@gmail",
        "https://randomuser.me/api/portraits/med/men/75.jpg",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
        "https://upload.wikimedia.org/wikipedia/commons/1/1d/Getting_the_coconut_the_Timorese_way.jpg",
        10,
        2,
        Date()
    ),
    ForumStatus(
        "John Doe 3",
        "johndoe@gmail",
        "https://randomuser.me/api/portraits/med/men/75.jpg",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
        "https://upload.wikimedia.org/wikipedia/commons/1/1d/Getting_the_coconut_the_Timorese_way.jpg",
        10,
        2,
        Date()
    ),
    ForumStatus(
        "John Doe",
        "johndoe@gmail",
        "https://randomuser.me/api/portraits/med/men/75.jpg",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
        "https://upload.wikimedia.org/wikipedia/commons/1/1d/Getting_the_coconut_the_Timorese_way.jpg",
        10,
        2,
        Date()
    )
)