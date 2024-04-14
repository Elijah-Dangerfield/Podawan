package com.dangerfield.libraries.storage.internal

import androidx.room.ProvidedTypeConverter
import com.squareup.moshi.Moshi
import javax.inject.Inject

@ProvidedTypeConverter
class Converters @Inject constructor(
    private val moshi: Moshi
) {

}