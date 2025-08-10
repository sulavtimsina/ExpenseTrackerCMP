package com.sulavtimsina.expensetracker.analytics.di

import com.sulavtimsina.expensetracker.analytics.domain.GetAnalyticsDataUseCase
import com.sulavtimsina.expensetracker.analytics.presentation.AnalyticsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val analyticsModule =
    module {
        single<GetAnalyticsDataUseCase> {
            GetAnalyticsDataUseCase(repository = get())
        }

        viewModel {
            AnalyticsViewModel(getAnalyticsDataUseCase = get())
        }
    }
