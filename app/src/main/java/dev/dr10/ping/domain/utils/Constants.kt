package dev.dr10.ping.domain.utils

object Constants {

    const val APP_DB = "ping_database"
    const val PROFILE_IMAGE_BUCKET = "profile-images"

    const val USER_PROFILE_PREFS = "user_profile_prefs"
    const val USER_PROFILE_ID = "user_id"
    const val USER_PROFILE_IMAGE_PATH = "profile_image_path"
    const val USER_PROFILE_USERNAME = "username"
    const val USER_PROFILE_BIO = "bio"
    const val USERS_TABLE = "users"
    const val USERNAME_COLUMN = "username"
    const val IS_ONLINE_COLUMN = "is_online"
    const val LAST_CONNECTED_COLUMN = "last_connected"
    const val PROFILE_IMAGE_COLUMN = "profile_image"
    const val CONVERSATIONS_TABLE = "conversations"
    const val MESSAGES_TABLE = "messages"
    const val TABLE_SCHEMA = "public"
    const val IS_LOGGED_IN = "is_logged_in"
    const val IS_PROFILE_SETUP_COMPLETED = "is_profile_setup_completed"

    const val RPC_FUNCTION_NAME = "get_random_users"
    const val RPC_FUNCTION_PARAM = "excluded_id"
    const val PROFILE_IMAGES_BUCKET_PATH = "/storage/v1/object/public/$PROFILE_IMAGE_BUCKET/"

    const val RPC_MESSAGES_NAME = "get_messages"
    const val RPC_MESSAGES_PARAM_CONVERSATION_ID = "conversation_id"
    const val RPC_MESSAGES_PARAM_LAST_TIMESTAMP = "last_timestamp"
    const val RPC_MESSAGES_PARAM_LIMIT_COUNT = "limit_count"

    const val RPC_UPSERT_CONVERSATION_NAME = "upsert_conversation"
    const val RPC_UPDATE_STATUS = "update_status"
    const val RPC_UPDATE_STATUS_PARAM_USER_ID = "p_user_id"
    const val RPC_UPDATE_STATUS_PARAM_IS_ONLINE = "p_is_online"

    // Error messages
    const val ERROR_USER_ALREADY_EXISTS = "User already registered"
    const val ERROR_CANCELLED_AUTH = "activity is cancelled by the user"
    const val ERROR_INVALID_CREDENTIALS = "Invalid login credentials"
}