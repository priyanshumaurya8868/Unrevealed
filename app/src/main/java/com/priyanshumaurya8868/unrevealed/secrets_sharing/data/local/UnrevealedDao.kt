package com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.entity.SecretEntity
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.entity.TagEntity
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.entity.UserProfileEntity

@Dao
interface UnrevealedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedSecrets(
        secrets: List<SecretEntity>
    )

    @Query("SELECT * FROM SecretEntity WHERE _id LIKE :id")
    suspend fun getSecretById(id :String):SecretEntity

    @Query( """SELECT *
            FROM secretentity
            WHERE LOWER(tag) LIKE '%'||LOWER(:tag) || '%'
            AND LOWER(author) LIKE '%'||LOWER(:author_id) || '%'
            LIMIT :limit 
            OFFSET :skip
            """)
    suspend fun getFeeds(tag : String, limit : Int , skip : Int, author_id:String): List<SecretEntity>

    @Query("DELETE  FROM secretentity  WHERE LOWER(tag) LIKE '%'||LOWER(:tag) || '%'")
    suspend fun clearFeedSecretList(tag : String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDetail(userProfileEntity: UserProfileEntity)

    @Query("SELECT * FROM UserProfileEntity WHERE _id = :userId")
    suspend fun getUserProfileById(userId: String): UserProfileEntity?

    @Query("DELETE FROM SecretEntity WHERE _id LIKE :id")
    suspend fun deleteById(id :String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTags(list : List<TagEntity>)

    @Query("SELECT * FROM TagEntity")
    suspend fun getTags() :List<TagEntity>

}