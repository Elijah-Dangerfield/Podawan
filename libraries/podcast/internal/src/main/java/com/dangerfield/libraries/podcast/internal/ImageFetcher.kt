package com.dangerfield.libraries.podcast.internal

import android.content.Context
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageFetcher @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val imageLoader by lazy {
        ImageLoader.Builder(context)
            .respectCacheHeaders(false)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    fun fetchImages(urls: List<String>) {
        urls.map { url ->
            ImageRequest.Builder(context)
                .data(url)
                .size(Size.ORIGINAL)
                .diskCacheKey(url)
                .memoryCacheKey(url)
                .build()
        }.forEach {
            imageLoader
                .enqueue(it)
        }
    }

    /**
     * removes an image with the given url key from cache
     */
    @OptIn(ExperimentalCoilApi::class)
    fun deleteImage(url: String) {
        imageLoader.diskCache?.remove(url)
        imageLoader.memoryCache?.remove(MemoryCache.Key(url))
    }
}