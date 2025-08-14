package com.example.a7minutesworkout.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class ExerciseListResponse(
    val data: Data?,
    val status: String?,
    val message: String?
) : Parcelable

@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class Data(
    val exerciseList: List<ExerciseModel>?
) : Parcelable