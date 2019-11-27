package com.food.nextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimeSlots (var morningTimeSlots: ArrayList<String>,
                      var afterNoonTimeSlots: ArrayList<String>,
                      var eveningTimeSlots: ArrayList<String>) : Parcelable