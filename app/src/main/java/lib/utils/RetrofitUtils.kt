package lib.utils

import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit

inline fun <reified ServiceType> buildRetrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient,
    converterFactory: Converter.Factory? = null,
    callAdapterFactory: CallAdapter.Factory? = null,
    noinline setup: (Retrofit.Builder.() -> Unit)? = null
) : ServiceType {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .apply {
            callAdapterFactory?.let {
                addCallAdapterFactory(callAdapterFactory)
            }
            converterFactory?.let {
                addConverterFactory(converterFactory)
            }
            setup?.invoke(this)
        }
        .build().create(ServiceType::class.java)
}