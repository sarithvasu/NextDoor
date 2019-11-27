package com.food.nextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class LauncherFeed(
    val apartments: List<LauncherApartment>,
    val cities: List<City>,
    val countries: List<Country>,
    val states: List<State>
):Parcelable
@Parcelize
data class LauncherApartment(
    val ActiveChefCount: Int,
    val ActiveUserCount: Int,
    val ApartmentId: Int,
    val ApartmentName: String,
    val CityId: Int,
    val PinCode: String
):Parcelable{
    @Override
    override fun toString(): String {
        return ApartmentName
    }
}
@Parcelize
data class City(
    val CityId: Int,
    val CityName: String,
    val StateId: Int
):Parcelable
{
    @Override
    override fun toString(): String {
        return CityName
    }
}
@Parcelize
data class Country(
    val CountryId: Int,
    val CountryName: String
):Parcelable
{
    @Override
    override fun toString(): String {
        return CountryName
    }
}
@Parcelize
data class State(
    val CountryId: Int,
    val StateId: Int,
    val StateName: String
):Parcelable
{
    @Override
    override fun toString(): String {
        return StateName
    }
}