package com.govtprep.di

import com.govtprep.data.remote.SupabaseDataSource
import com.govtprep.data.repository.AuthRepository
import com.govtprep.data.repository.TestRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSupabaseDataSource(): SupabaseDataSource = SupabaseDataSource()

    @Provides
    @Singleton
    fun provideAuthRepository(dataSource: SupabaseDataSource): AuthRepository = AuthRepository(dataSource)

    @Provides
    @Singleton
    fun provideTestRepository(dataSource: SupabaseDataSource): TestRepository = TestRepository(dataSource)
}
