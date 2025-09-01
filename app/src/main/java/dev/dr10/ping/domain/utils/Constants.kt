package dev.dr10.ping.domain.utils

object Constants {

    const val PROFILE_IMAGE_BUCKET = "profile-images"

    const val USER_PROFILE_PREFS = "user_profile_prefs"
    const val USER_PROFILE_ID = "user_id"
    const val USER_PROFILE_IMAGE_PATH = "profile_image_path"
    const val USER_PROFILE_USERNAME = "username"
    const val USER_PROFILE_BIO = "bio"
    const val USERS_TABLE = "users"
    const val MESSAGES_TABLE = "messages"
    const val IS_LOGGED_IN = "is_logged_in"
    const val IS_PROFILE_SETUP_COMPLETED = "is_profile_setup_completed"

    const val RPC_FUNCTION_NAME = "get_random_users"
    const val RPC_FUNCTION_PARAM = "excluded_id"
    const val PROFILE_IMAGES_BUCKET_PATH = "/storage/v1/object/public/$PROFILE_IMAGE_BUCKET/"

    // Error messages
    const val ERROR_USER_ALREADY_EXISTS = "User already registered"
    const val ERROR_CANCELLED_AUTH = "activity is cancelled by the user"
    const val ERROR_INVALID_CREDENTIALS = "Invalid login credentials"
}