package lib.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.nio.Buffer
import kotlin.reflect.full.hasAnnotation

class ContentConverterFactory(val moshi: Moshi): Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val jsonAnnotation = annotations.filter { it.annotationClass.hasAnnotation<JsonQualifier>() }.toSet()
        val adapter = moshi.adapter<Any>(type, jsonAnnotation)
        return ContentResponseBodyConverter(adapter)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        val jsonAnnotation = parameterAnnotations.filter { it.annotationClass.hasAnnotation<JsonQualifier>() }.toSet()
        val adapter = moshi.adapter<Any>(type, jsonAnnotation)
        return MoshiRequestBodyConverter(adapter)
    }
}

class ContentResponseBodyConverter<T>(private val adapter: JsonAdapter<T>): Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T? {
        return value.string().let {
            it pipe adapter::fromJson
        }
    }

}

class MoshiRequestBodyConverter<T>(private val adapter: JsonAdapter<T>): Converter<T, RequestBody> {
    companion object {
        val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaType()
    }

    override fun convert(value: T): RequestBody? {
        val buffer = okio.Buffer()
        val writer = JsonWriter.of(buffer)
        adapter.toJson(writer, value)
        return buffer.readByteString().toRequestBody(MEDIA_TYPE)
    }
}


inline infix fun <P1, R> P1.pipe(t: (P1) -> R): R = t(this)