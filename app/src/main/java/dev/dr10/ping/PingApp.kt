package dev.dr10.ping

import android.app.Application
import dev.dr10.ping.data.di.dataModule
import dev.dr10.ping.domain.di.domainModule
import dev.dr10.ping.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PingApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PingApp)
            modules(dataModule, domainModule, uiModule)
        }
    }
}