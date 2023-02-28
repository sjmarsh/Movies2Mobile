package com.sjmarsh.movies2mobile

import android.app.Application
import com.sjmarsh.movies2mobile.data.IDataService
import com.sjmarsh.movies2mobile.data.DataService
import com.sjmarsh.movies2mobile.data.IDataStore
import com.sjmarsh.movies2mobile.data.DataStore
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
    var appModule = module {
        single<IDataStore> { DataStore(androidContext()) }
        single<IDataService> { DataService(get()) }
    }
}