package com.daniel.ping.domain.models

data class User(
    val id: String,
    val name: String,
    val description: String,
    val profileImage: String,
    val token: String
)