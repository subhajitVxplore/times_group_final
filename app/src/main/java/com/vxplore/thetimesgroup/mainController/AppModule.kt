package com.vxplore.thetimesgroup.mainController

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.vxplore.thetimesgroup.utility.bluetoothService.AndroidBluetoothController
import com.vxplore.core.domain.repositoriess.*
import com.vxplore.core.helpers.AppStore
import com.vxplore.core.helpers.Info
import com.vxplore.thetimesgroup.data.online.AppVersionApi
import com.vxplore.thetimesgroup.helpers_impl.AppInfo
import com.vxplore.thetimesgroup.helpers_impl.AppStoreImpl
import com.vxplore.thetimesgroup.repository_impls.*
import com.vxplore.thetimesgroup.utility.Constants
import com.vxplore.thetimesgroup.utility.Metar
import com.vxplore.thetimesgroup.utility.bluetoothService.BluetoothController
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    companion object {
        private val Context.dataStore by preferencesDataStore("timesGroup")

        private fun <T> provideApi(klass: Class<T>): T {
            val okHttpClient = OkHttpClient.Builder().addInterceptor(
                ChuckerInterceptor.Builder(HiltControllerApp.app!!.applicationContext)
                    .collector(ChuckerCollector(HiltControllerApp.app!!.applicationContext))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            ).build()

            return Retrofit.Builder()
                .baseUrl(Metar[Constants.BASE_URL])
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(klass)
        }



//-----------------------------------------------------------------------------------------------------
        private fun <T> provideApi2(klass: Class<T>): T {
            val okHttpClient = OkHttpClient.Builder().addInterceptor(
                ChuckerInterceptor.Builder(HiltControllerApp.app!!.applicationContext)
                    .collector(ChuckerCollector(HiltControllerApp.app!!.applicationContext))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            ).build()

            return Retrofit.Builder()
                .baseUrl("https://api.npoint.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(klass)
        }
        @Singleton
        @Provides
        fun provideAppVersion(): AppVersionApi = provideApi2(AppVersionApi::class.java)


        @Provides
        @Singleton
        fun provideBluetoothController(@ApplicationContext context: Context): BluetoothController {
            return AndroidBluetoothController(context)
        }

        //-----------------------------------------------------------------------------------------




        @Singleton
        @Provides
        fun provideDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> =
            appContext.dataStore

        @Singleton
        @Provides
        fun provideBaseUrl(): MyApiList = provideApi(MyApiList::class.java)

    }//companion object
////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Binds
    fun bindAppStore(appStoreImpl: AppStoreImpl): AppStore

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Binds
    fun bindSplashRepo(impl: SplashRepositoryImpl): SplashRepository

    @Binds
    fun bindBaseUrlRepo(impl: BaseUrlRepositoryImpl): BaseUrlRepository

    @Binds
    fun bindAppInfo(appInfo: AppInfo): Info

    @Binds
    fun bindMobileNoScreenRepo(impl: MobileNoScreenRepositoryImpl): MobileNoScreenRepository
    @Binds
    fun bindOtpRepo(impl: OtpRepositoryImpl): OtpRepository
    @Binds
    fun bindRegisterRepo(impl: RegisterRepositoryImpl): RegisterRepository
    @Binds
    fun bindAddVendorRepo(impl: AddVendorRepositoryImpl): AddVendorRepository

    @Binds
    fun bindDistributorDetailsRepo(impl: DistributorDetailsRepositoryImpl): DistributorDetailsRepository

    @Binds
    fun bindsearchVendorRepo(impl: SearchVendorRepositoryImpl): SearchVendorRepository

    @Binds
    fun bindPapersByVendorIdRepo(impl: PapersByVendorIdRepositoryImpl): PapersByVendorIdRepository

    @Binds
    fun bindTodayPaperSoldByUserIdRepo(impl: TodayPaperSoldByUserIdRepositoryImpl): TodayPaperSoldByUserIdRepository

    @Binds
    fun bindGenerateBillRepo(impl: GenerateBillRepositoryImpl): GenerateBillRepository






//-------------------------------------------------------------------------------------------
    @Binds
    fun bindVendorDetailsRepo(impl: VendorDetailsRepositoryImpl): VendorDetailsRepository

    @Binds
    fun bindDonutChartDetailsRepo(impl: DonutChartDetailsRepositoryImpl): DonutChartDetailsRepository



//-------------------------------------------------------------------------------------------

}