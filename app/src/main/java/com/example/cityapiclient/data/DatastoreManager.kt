package com.example.cityapiclient.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton //You can ignore this annotation as return `datastore` from `preferencesDataStore` is singletone
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {


}
