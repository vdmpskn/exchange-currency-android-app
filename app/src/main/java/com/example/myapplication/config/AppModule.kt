package com.example.myapplication.config

import com.example.myapplication.service.ApiService
import com.example.myapplication.service.ExchangeService
import com.example.myapplication.ui.UIHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { ApiService() }

    single { ExchangeService() }

    single { UIHelper(androidContext()) }
}