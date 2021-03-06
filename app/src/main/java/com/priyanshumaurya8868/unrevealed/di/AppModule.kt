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
import com.priyanshumaurya8868.unrevealed.auth.data.local.AuthDataBase
import com.priyanshumaurya8868.unrevealed.auth.data.remote.services.AuthService
import com.priyanshumaurya8868.unrevealed.auth.data.remote.services.AuthServiceImpl
import com.priyanshumaurya8868.unrevealed.auth.data.repo.UnrevealedAuthRepoImpl
import com.priyanshumaurya8868.unrevealed.auth.domain.repo.UnrevealedAuthRepo
import com.priyanshumaurya8868.unrevealed.auth.domain.usecase.*
import com.priyanshumaurya8868.unrevealed.auth.domain.usecase.GetLoggedUser
import com.priyanshumaurya8868.unrevealed.core.ThemeSwitcher
import com.priyanshumaurya8868.unrevealed.core.utils.Constants.USER_PREFERENCES
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
    fun provideAuthService(client: HttpClient, dataStore: DataStore<Preferences>): AuthService {
        return AuthServiceImpl(client,dataStore)
    }

    @Provides
    @Singleton
    fun provideUnrevealedAuthRepo(service: AuthService, db : AuthDataBase,dataStore: DataStore<Preferences>): UnrevealedAuthRepo {
        return UnrevealedAuthRepoImpl(service,db,dataStore)
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
    fun provideAuthUseCase(repo: UnrevealedAuthRepo, dataStore: DataStore<Preferences>, authDataBase: AuthDataBase) =
        AuthUseCases(
            login = Login(repo),
            signup = Signup(repo),
            getAvatars = GetAvatars(repo),
            savePreferences = SavePreferences(dataStore),
            getLoggedUser = GetLoggedUser(repo),
            removeAccountByID = RemoveAccountByID(repo),
            changeAvatar = ChangeAvatar(repo),
            changePassword = ChangePassword(repo),
            deactivateAccount = DeactivateAccount(repo),
            getMyProfileByID = GetMyProfileByID(authDataBase)
        )

    @Provides
    @Singleton
    fun provideUnrevealedApi(client: HttpClient, dataStore: DataStore<Preferences>): UnrevealedApi =
        UnrevealedApiImpl(client, dataStore)


    @Provides
    @Singleton
    fun providesSecretsDb(app: Application): SecretsDatabase {
        return Room.databaseBuilder(app, SecretsDatabase::class.java, "secrets.db")
            .addTypeConverter(UserProfileTypeConverter()).build()
    }

    @Provides
    @Singleton
    fun provideAuthDB(app: Application):AuthDataBase{
        return Room.databaseBuilder(app, AuthDataBase::class.java, "unrevealed_auth.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideSecretSharingRepository(
        api: UnrevealedApi,
        secretsDatabase: SecretsDatabase,
        authDataBase: AuthDataBase,
        dataStore: DataStore<Preferences>
    ): Repository =
        RepositoryImpl(api = api, dataStore = dataStore, secretsDatabase =secretsDatabase , authDataBase = authDataBase )


    @Provides
    @Singleton
    fun provideSecretsSharingUseCases(
        repo: Repository,
        dataStore: DataStore<Preferences>,
        authDataBase: AuthDataBase,
        secretsDatabase: SecretsDatabase,
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
            logOut = LogOut(repo,dataStore, authDataBase),
            getLoggedUser = com.priyanshumaurya8868.unrevealed.secrets_sharing.domain.usecases.GetLoggedUser(repo),
            switchAccount = SwitchAccount(dataStore),
            deleteCommentOrReply = DeleteCommentOrReply(repo),
            deleteSecret = DeleteSecret(repo),
            updateComment = UpdateComment(repo),
            updateReply = UpdateReply(repo),
            updateSecret = UpdateSecret(repo),
            getTags = GetTags(repo,secretsDatabase)
        )


    @Provides
    @Singleton
    fun provideThemeSwitcher(dataStore: DataStore<Preferences>) = ThemeSwitcher(dataStore)

}

