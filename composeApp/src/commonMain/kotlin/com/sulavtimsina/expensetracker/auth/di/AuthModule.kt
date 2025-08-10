package com.sulavtimsina.expensetracker.auth.di

import com.sulavtimsina.expensetracker.auth.data.AuthRepository
import com.sulavtimsina.expensetracker.auth.presentation.AuthViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authModule =
    module {
        singleOf(::AuthRepository)
        singleOf(::AuthViewModel)
    }
