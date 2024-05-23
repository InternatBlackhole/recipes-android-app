package si.uni_lj.fri.pbd.miniapp3.rest

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import si.uni_lj.fri.pbd.miniapp3.Constants

object ServiceGenerator {
    private var sBuilder: Retrofit.Builder =
        Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        )

    private var sHttpClient: OkHttpClient.Builder =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor {
                Log.v("MiniApp3HTTP", it)
            }.apply { level = HttpLoggingInterceptor.Level.BASIC })

    private var sRetrofit: Retrofit = sBuilder.client(sHttpClient.build()).build()

    fun <S> createService(serviceClass: Class<S>): S {
        return sRetrofit.create(serviceClass)
    }
}