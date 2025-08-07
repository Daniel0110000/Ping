package dev.dr10.ping.di

import dev.dr10.ping.domain.utils.Constants
import io.appwrite.Client
import io.appwrite.services.Account
import org.koin.dsl.module

val appWriteModule = module {
    single<Client> { Client(get()).setEndpoint(Constants.APPWRITE_ENDPOINT).setProject(Constants.APPWRITE_PROJECT_ID).setSelfSigned(true) }
    single<Account> { Account(get()) }
}