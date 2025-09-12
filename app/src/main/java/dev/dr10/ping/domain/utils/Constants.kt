package dev.dr10.ping.domain.utils

object Constants {

    const val NOTIFICATIONS_CHANNEL_ID = "ping_chat_notifications"
    const val NOTIFICATIONS_CHANNEL_NAME = "Ping Chat Notifications"
    const val NOTIFICATION_DEEP_LINK_BASE_URL = "ping://chat/"

    const val APP_DB = "ping_database"
    const val PROFILE_IMAGE_BUCKET = "profile-images"

    const val USER_PROFILE_PREFS = "user_profile_prefs"
    const val USER_PROFILE_ID = "user_id"
    const val USER_PROFILE_IMAGE_PATH = "profile_image_path"
    const val USER_PROFILE_IMAGE_NAME = "profile_image_name"
    const val USER_PROFILE_USERNAME = "username"
    const val USER_PROFILE_BIO = "bio"
    const val USERS_TABLE = "users"
    const val USERNAME_COLUMN = "username"
    const val PROFILE_IMAGE_COLUMN = "profile_image"
    const val FCM_TOKEN_COLUMN = "fcm_token"
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

    const val USER_PRESENCES_TABLE = "user_presences"
    const val RPC_UPDATE_LAST_CONNECTED = "update_last_connected"
    const val RPC_UPDATE_USER_STATUS = "update_user_status"
    const val RPC_USER_ID_PARAM = "p_user_id"
    const val RPC_IS_ONLINE_PARAM = "p_is_online"

    const val ACTIVE_RECENTLY_MESSAGE = "Active recently"

    // Error messages
    const val ERROR_USER_ALREADY_EXISTS = "User already registered"
    const val ERROR_CANCELLED_AUTH = "activity is cancelled by the user"
    const val ERROR_INVALID_CREDENTIALS = "Invalid login credentials"
}