package dev.dr10.ping.ui.extensions

import androidx.compose.runtime.snapshots.SnapshotStateList

fun <T> SnapshotStateList<T>.addIfNotExists(item: T) {
    if (!this.contains(item)) this.add(item)
}