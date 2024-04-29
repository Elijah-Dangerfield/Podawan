package com.dangerfield.libraries.storage.internal

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import podawan.core.readJson
import podawan.core.toJson
import javax.inject.Inject

@ProvidedTypeConverter
class Converters @Inject constructor(
    private val moshi: Moshi
) {

    @TypeConverter
    fun fromString(value: String?): List<String> {
        return if (value == null) {
            emptyList()
        } else {
            moshi.readJson(value) ?: emptyList()
        }
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return moshi.toJson(list)
    }
}