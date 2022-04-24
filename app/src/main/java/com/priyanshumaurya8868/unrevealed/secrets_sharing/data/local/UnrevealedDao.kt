package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.entity.SecretEntity
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.entity.UserProfileEntity

@Dao
interface UnrevealedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedSecrets(
        secrets: List<SecretEntity>
    )
    @Query("SELECT * FROM SecretEntity")
    suspend fun getFeeds():List<SecretEntity>

    @Query("DELETE  FROM secretentity")
    suspend fun clearFeedSecretList()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetail(userProfileEntity: UserProfileEntity)

    @Query("SELECT * FROM UserProfileEntity WHERE _id = :userId")
    suspend fun getUserProfileById(userId: String) : UserProfileEntity?




}