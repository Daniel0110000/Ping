package dev.dr10.ping

import android.app.Application
import dev.dr10.ping.di.repositoriesModule
import dev.dr10.ping.di.supabaseModule
import dev.dr10.ping.di.useCasesModule
import dev.dr10.ping.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PingApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PingApp)
            modules(supabaseModule, repositoriesModule, useCasesModule, viewModelsModule)
        }
    }
}