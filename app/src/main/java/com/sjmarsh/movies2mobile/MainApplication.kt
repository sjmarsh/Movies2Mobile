package com.sjmarsh.movies2mobile

import android.app.Application
import com.sjmarsh.movies2mobile.data.IDataService
import com.sjmarsh.movies2mobile.data.DataService
import com.sjmarsh.movies2mobile.data.IDataStorage
import com.sjmarsh.movies2mobile.data.IJsonToModel
import com.sjmarsh.movies2mobile.data.JsonToModel
import com.sjmarsh.movies2mobile.data.databaseStorage.DbDataStorage
import com.sjmarsh.movies2mobile.data.fileStorage.FileDataStorage
import com.sjmarsh.movies2mobile.data.fileStorage.MovieFile
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

    // dependency injection setup
    private var appModule = module {
        single<IJsonToModel> { JsonToModel() }
        //single<IDataStorage> { FileDataStorage(MovieFile(androidContext())) }
        single<IDataStorage> { DbDataStorage(androidContext()) }
        single<IDataService> { DataService(get()) }
    }
}