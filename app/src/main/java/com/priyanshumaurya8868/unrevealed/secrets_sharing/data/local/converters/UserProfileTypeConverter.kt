package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.entity.UserProfileEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ProvidedTypeConverter
class UserProfileTypeConverter {
    @TypeConverter
    fun stringToObj(string: String): UserProfileEntity {
        return Json.decodeFromString(string)
    }

    @TypeConverter
    fun objToString(obj: UserProfileEntity?): String {
        return Json.encodeToString(obj)
    }
}