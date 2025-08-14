package com.example.a7minutesworkout.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class ExerciseModel(
    val id: Int?,
    val name : String?,
    @Json(name = "imageUrl")
    val image : String?,
    var isCompleted : Boolean? = false,
    var isSelected : Boolean? = false
) : Parcelable