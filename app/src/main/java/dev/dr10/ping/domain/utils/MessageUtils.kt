package dev.dr10.ping.domain.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.Locale

object MessageUtils {

    /**
     * Generate a unique chat ID based on sender and receiver IDs
     *
     * @param senderId The ID of the sender
     * @param receiver The ID of the receiver
     */
    fun generateChatId(senderId: String, receiver: String): String {
        val (userIdA, userIdB) = listOf(senderId, receiver).sorted()
        return "$userIdA:$userIdB"
    }

    /**
     * Convert UTC date string to local time
     *
     * @param createdAt The UTC date string to convert
     * @return The local time string
     */
    fun convertUtcToLocalTime(createdAt: String): String {
        val formatter = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .toFormatter()

        val localDateTime: LocalDateTime = LocalDateTime.parse(createdAt, formatter)

        val utcZoned = localDateTime.atZone(ZoneId.of("UTC"))
        val localZoned = utcZoned.withZoneSameInstant(ZoneId.systemDefault())

        val outputFormatter = DateTimeFormatter.ofPattern("MMMM d - h:mm a", Locale.getDefault())
        return localZoned.format(outputFormatter)
    }

}