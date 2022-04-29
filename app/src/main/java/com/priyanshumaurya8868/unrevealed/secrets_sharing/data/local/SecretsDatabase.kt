package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.converters.UserProfileTypeConverter
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.entity.SecretEntity
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.entity.UserProfileEntity

@Database(
    entities = [SecretEntity::class, UserProfileEntity::class],
    version = 1
)
@TypeConverters(UserProfileTypeConverter::class)
abstract class SecretsDatabase : RoomDatabase() {

    abstract val dao: UnrevealedDao

}