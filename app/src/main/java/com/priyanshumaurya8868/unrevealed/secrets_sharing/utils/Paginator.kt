package com.priyanshumaurya8868.unrevealed.secrets_sharing.utils

interface Paginator<Key, Item> {
    suspend fun loadNextItem()
    fun reset()
}