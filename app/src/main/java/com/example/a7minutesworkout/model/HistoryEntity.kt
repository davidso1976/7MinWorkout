package com.example.a7minutesworkout.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@Keep
@JsonClass(generateAdapter = true)
@Parcelize

data class HistoryEntity(
    val date:String
) : Parcelable
