package com.priyanshumaurya8868.unrevealed.secrets_sharing.utils

import com.priyanshumaurya8868.unrevealed.core.Resource
import kotlinx.coroutines.flow.Flow

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean, List<Item>?) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Flow<Resource<List<Item>>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (data: List<Item>?, message: String) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
) : Paginator<Key, Item> {
    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItem() {
        if (isMakingRequest)
            return
        isMakingRequest = true
        val result = onRequest(currentKey)
        result.collect {
            when (it) {
                is Resource.Loading -> {
                    onLoadUpdated(true, it.data)
                }
                is Resource.Success -> {

                    onSuccess(it.data ?: emptyList(), currentKey)
                    currentKey = getNextKey(it.data ?: emptyList())
                }
                is Resource.Error -> {
                    onError(it.data, it.message ?: "Something went wrong!")
                }
            }
        }
        onLoadUpdated(false, null)
        isMakingRequest = false
    }

    override fun reset() {
        currentKey = initialKey
    }
}