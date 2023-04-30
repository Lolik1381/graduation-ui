package ru.stankin.compose.datasource

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.stankin.compose.datasource.converter.ZonedDateTimeConverter
import java.time.ZonedDateTime

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            val gsonBuilder = GsonBuilder()
                .setLenient()
                .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeConverter())
                .create()
            val gsonConverterFactory = GsonConverterFactory.create(gsonBuilder)

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(gsonConverterFactory)
                .client(httpClient())
                .build()
        }
        return retrofit!!
    }

    private fun httpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }
}