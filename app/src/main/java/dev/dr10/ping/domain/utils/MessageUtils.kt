package dev.dr10.ping.domain.utils

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.Locale

object MessageUtils {

    private val inputFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
        .optionalEnd()
        .toFormatter()

    /**
     * Generate a unique chat ID based on sender and receiver IDs
     *
     * @param senderId The ID of the sender
     * @param receiver The ID of the receiver
     */
    fun generateConversationId(senderId: String, receiver: String): String {
        val (userIdA, userIdB) = listOf(senderId, receiver).sorted()
        return "$userIdA:$userIdB"
    }

    /**
     * Convert a UTC date-time string to the local date-time
     *
     * @param createdAt The UTC date-time string to convert
     * @return The converted local date-time
     */
    fun convertUtcToLocalDateTime(createdAt: String): LocalDateTime {
        val localDateTime = LocalDateTime.parse(createdAt, inputFormatter)
        val utcZoned = localDateTime.atZone(ZoneId.of("UTC"))
        val localZoned = utcZoned.withZoneSameInstant(ZoneId.systemDefault())
        return localZoned.toLocalDateTime()
    }

    /**
     * Convert a UTC date-time string to a formatted local date-time
     *
     * @param createdAt The UTC date-time string to convert
     * @return The converted and formatted local date-time
     */
    fun convertUtcToLocalTime(createdAt: String): String {
        val localDateTime = convertUtcToLocalDateTime(createdAt)
        val outputFormatter = DateTimeFormatter.ofPattern("MMMM d - h:mm a", Locale.getDefault())
        return localDateTime.format(outputFormatter)
    }

    /**
     * Format the last connected time based on the current time
     *
     * @param utcDate The UTC date-time string to format
     * @return The formatted last connected time
     */
    fun formatLastConnected(utcDate: String): String {
        val lastConnected = convertUtcToLocalDateTime(utcDate)
        val now = LocalDateTime.now()
        val duration = Duration.between(lastConnected, now)

        val seconds = duration.seconds
        val minutes = duration.toMinutes()
        val hours = duration.toHours()
        val days = duration.toDays()

        return when {
            seconds < 60 -> "Active $seconds second${if (seconds != 1L) "s" else ""} ago"
            minutes < 60 -> "Active $minutes minute${if (minutes != 1L) "s" else ""} ago"
            hours < 24 -> "Active $hours hour${if (hours != 1L) "s" else ""} ago"
            days <= 2 -> "Active $days day${if (days != 1L) "s" else ""} ago"
            else -> lastConnected.format(DateTimeFormatter.ofPattern("MMMM d - h:mm a", Locale.getDefault()))
        }
    }

    /**
     * Extract the user ID based on the current user and sender/receiver IDs
     *
     * @param currentUserId The ID of the current user
     * @param senderId The ID of the sender
     * @param receiverId The ID of the receiver
     * @return The extracted user ID
     */
    fun extractUserId(currentUserId: String, senderId: String, receiverId: String): String =
        if (senderId == currentUserId) receiverId else senderId
}