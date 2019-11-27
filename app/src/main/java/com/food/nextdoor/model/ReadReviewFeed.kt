package com.food.nextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ReadReviewFeed ( val reviews : List<Review>) : Parcelable

