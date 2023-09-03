package com.dapascript.pokecat.di

import android.content.Context
import androidx.room.Room
import com.dapascript.pokecat.data.repository.PokemonRepository
import com.dapascript.pokecat.data.repository.PokemonRepositoryImpl
import com.dapascript.pokecat.data.source.local.PokemonDao
import com.dapascript.pokecat.data.source.local.PokemonDatabase
import com.dapascript.pokecat.data.source.remote.ApiService
import com.facebook.shimmer.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (!BuildConfig.DEBUG) HttpLoggingInterceptor.Level.NONE
        else HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideApiService(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): ApiService = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(ApiService::class.java)

    @Provides
    fun providePokemonDatabase(@ApplicationContext context: Context): PokemonDatabase =
        Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "pokemon.db"
        ).build()

    @Provides
    fun providePokemonDao(pokemonDB: PokemonDatabase): PokemonDao = pokemonDB.pokemonDao()

    @Provides
    fun provideRepository(
        apiService: ApiService,
        pokemonDao: PokemonDao
    ): PokemonRepository = PokemonRepositoryImpl(apiService, pokemonDao)
}