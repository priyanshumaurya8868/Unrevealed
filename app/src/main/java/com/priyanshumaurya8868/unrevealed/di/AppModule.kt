package com.priyanshumaurya8868.unrevealed.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.priyanshumaurya8868.unrevealed.auth.data.remote.services.AuthService
import com.priyanshumaurya8868.unrevealed.auth.data.remote.services.AuthServiceImpl
import com.priyanshumaurya8868.unrevealed.auth.data.repo.UnrevealedAuthRepoImpl
import com.priyanshumaurya8868.unrevealed.auth.domain.repo.UnrevealedAuthRepo
import com.priyanshumaurya8868.unrevealed.auth.domain.usecase.*
import com.priyanshumaurya8868.unrevealed.core.Constants.USER_PREFERENCES
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.SecretsDatabase
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.local.converters.UserProfileTypeConverter
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.service.UnrevealedApi
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.remote.service.UnrevealedApiImpl
import com.priyanshumaurya8868.unrevealed.secrets_sharing.data.repository.RepositoryImpl
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.repo.Repository
import com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(JsonFeature) {
                serializer =
                    KotlinxSerializer(kotlinx.serialization.json.Json { ignoreUnknownKeys = true })
            }
        }
    }

    @Provides
    @Singleton
    fun provideAuthService(client: HttpClient): AuthService {
        return AuthServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideUnrevealedAuthRepo(service: AuthService): UnrevealedAuthRepo {
        return UnrevealedAuthRepoImpl(service)
    }

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(appContext, USER_PREFERENCES)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }

    @Provides
    @Singleton
    fun provideAuthUseCase(repo: UnrevealedAuthRepo, dataStore: DataStore<Preferences>) =
        AuthUseCases(
            login = Login(repo),
            signup = Signup(repo),
            getAvatars = GetAvatars(repo),
            savePreferences = SavePreferences(dataStore)
        )

    @Provides
    @Singleton
    fun provideUnrevealedApi(client: HttpClient, dataStore: DataStore<Preferences>): UnrevealedApi =
        UnrevealedApiImpl(client, dataStore)


    @Provides
    @Singleton
    fun providesDb(app: Application): SecretsDatabase {
        return Room.databaseBuilder(app, SecretsDatabase::class.java, "secrets.db")
            .addTypeConverter(UserProfileTypeConverter()).build()
    }

    @Provides
    @Singleton
    fun provideSecretSharingRepository(
        api: UnrevealedApi,
        db: SecretsDatabase,
        dataStore: DataStore<Preferences>
    ): Repository =
        RepositoryImpl(api = api, db = db, dataStore = dataStore)


    @Provides
    @Singleton
    fun provideSecretsSharingUseCases(
        repo: Repository,
        dataStore: DataStore<Preferences>
    ) =
        SecretSharingUseCases(
            getFeeds = GetFeeds(repo),
            openCompleteSecret = OpenCompleteSecret(repo),
            postComment = PostComment(repo),
            likeComment = LikeComment(repo),
            dislikeComment = DislikeComment(repo),
            getMyProfile = GetMyProfile(repo),
            getUserById = GetUserById(repo),
            revealSecret = RevealSecret(repo),
            getComments = GetComments(repo),
            getReplies = GetReplies(repo),
            reactOnReply = ReactOnReply(repo),
            replyComment = ReplyComment(repo),
            logOut = LogOut(dataStore)
        )
}

