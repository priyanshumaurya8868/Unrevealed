package com.priyanshumaurya8868.unrevealed.auth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.priyanshumaurya8868.unrevealed.auth.data.remote.services.AuthService
import com.priyanshumaurya8868.unrevealed.auth.data.remote.services.AuthServiceImpl
import com.priyanshumaurya8868.unrevealed.auth.data.repo.UnrevealedAuthRepoImpl
import com.priyanshumaurya8868.unrevealed.auth.domain.repo.UnrevealedAuthRepo
import com.priyanshumaurya8868.unrevealed.auth.domain.usecase.*
import com.priyanshumaurya8868.unrevealed.utils.Constants.USER_PREFERENCES
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
object AuthModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer()
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
            saveToken = SaveToken(dataStore)
        )
}

