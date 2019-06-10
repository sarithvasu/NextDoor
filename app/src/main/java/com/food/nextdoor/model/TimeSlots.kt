package com.food.nextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimeSlots (var morningTimeSlots: ArrayList<String>,var afterNoonTimeSlots: ArrayList<String>,var evevingTimeSlots: ArrayList<String>) :
    Parcelable