package com.example.cryptoapp.di

import androidx.lifecycle.ViewModel
import com.example.cryptoapp.presentation.CoinViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.migration.DisableInstallInCheck
import dagger.multibindings.IntoMap

@DisableInstallInCheck
@Module
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(CoinViewModel::class)
    @Binds
    fun bindCoinViewModel(impl: CoinViewModel): ViewModel
}