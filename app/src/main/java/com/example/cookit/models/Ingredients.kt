package com.example.cookit.models

import android.os.Parcel
import android.os.Parcelable


data class Ingredients(val id: Int, val name: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ingredients> {
        override fun createFromParcel(parcel: Parcel): Ingredients {
            return Ingredients(parcel)
        }

        override fun newArray(size: Int): Array<Ingredients?> {
            return arrayOfNulls(size)
        }
    }
}