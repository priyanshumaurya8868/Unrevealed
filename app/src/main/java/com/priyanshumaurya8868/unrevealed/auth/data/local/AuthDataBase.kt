package com.priyanshumaurya8868.unrevealed.auth.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.priyanshumaurya8868.unrevealed.auth.data.local.enity.MyProfileEntity

@Database(
    entities = [MyProfileEntity::class],
    version = 1
)
abstract class AuthDataBase : RoomDatabase() {
    abstract val dao: AuthDao
}