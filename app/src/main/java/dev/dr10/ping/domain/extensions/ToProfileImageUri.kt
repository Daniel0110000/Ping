package dev.dr10.ping.domain.extensions

import dev.dr10.ping.BuildConfig
import dev.dr10.ping.domain.utils.Constants

fun String.toProfileImageUrl(): String =  "${BuildConfig.SUPABASE_URL}${Constants.PROFILE_IMAGES_BUCKET_PATH}$this"