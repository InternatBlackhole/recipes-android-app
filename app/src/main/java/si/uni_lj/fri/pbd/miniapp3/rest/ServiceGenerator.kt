package si.uni_lj.fri.pbd.miniapp3.rest

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import si.uni_lj.fri.pbd.miniapp3.Constants

object ServiceGenerator {
    private val sBuilder: Retrofit.Builder =
        Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
            )
        )

    private val httpClient: OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor {
                Log.v("MiniApp3HTTP", it)
            }.apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()

    private val sRetrofit: Retrofit = sBuilder.client(httpClient).build()

    fun <S> createService(serviceClass: Class<S>): S {
        return sRetrofit.create(serviceClass)
    }

    fun createImageUrlCall(url: String): Call {
        val request = Request.Builder()
            .url(url)
            .header("Content-Type", "image/jpeg")
            .build()

        return httpClient.newCall(request)
    }
}