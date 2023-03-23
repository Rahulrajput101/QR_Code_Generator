package com.ondevop.qrcodegenerator.di

import android.content.Context
import android.util.TypedValue
import androidx.room.Room
import com.ondevop.qrcodegenerator.db.QrDatabase
import com.ondevop.qrcodegenerator.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun qrDatabase(
        @ApplicationContext app : Context
    ) = Room.databaseBuilder(
        app,
        QrDatabase::class.java,
        QrDatabase.QR_DATABASE_NAME
    ).fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun provideRepository(db : QrDatabase) : Repository{
        return Repository(db.qrDao)
    }






}