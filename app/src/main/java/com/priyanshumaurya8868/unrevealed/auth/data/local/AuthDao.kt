package com.priyanshumaurya8868.unrevealed.auth.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.priyanshumaurya8868.unrevealed.auth.data.local.enity.MyProfileEntity

@Dao
interface AuthDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(myProfileEntity: MyProfileEntity)

    @Query("SELECT * FROM MyProfileEntity")
    suspend fun getMyProfiles() : List<MyProfileEntity>

    @Query("DELETE FROM MyProfileEntity WHERE user_id LIKE :id")
    suspend fun removeProfile(id : String)

    @Query("SELECT * FROM MyProfileEntity WHERE user_id like :id")
    suspend fun getProfileById(id:String):MyProfileEntity


}