package com.daniel.ping.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val description: String,
    val profileImageUrl: String = "",
    val token: String = ""
) : Parcelable