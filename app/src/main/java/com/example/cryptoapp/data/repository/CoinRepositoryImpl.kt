package com.example.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.database.CoinInfoDbModel
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.workers.RefreshDataWorker
import com.example.cryptoapp.domain.CoinInfo
import com.example.cryptoapp.domain.CoinRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val application: Application,

    private val mapper: CoinMapper
) : CoinRepository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinPriceInfoDao()

    override fun getCoinInfoList(): LiveData<List<CoinInfo>> {
        return MediatorLiveData<List<CoinInfo>>().apply {
            addSource(coinInfoDao.getPriceList()) {
                value = mapper.mapDbModelListToEntityList(it)
            }
        }

    }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfo> {
        return MediatorLiveData<CoinInfo>().apply {
            addSource(coinInfoDao.getPriceInfoAboutCoin(fromSymbol)) {
                value = mapper.mapDbModelToEntity(it)
            }
        }

    }

    override fun loadData() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniqueWork(
            RefreshDataWorker.NAME,
            ExistingWorkPolicy.REPLACE,
            RefreshDataWorker.makeRequest()
        )
    }
}