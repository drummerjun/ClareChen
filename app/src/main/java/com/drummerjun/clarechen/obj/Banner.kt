package com.drummerjun.clarechen.obj

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by drummerjun on 1/13/2018.
 */

class Banner(
        var title: String = "",
        var subscript: String = "",
        var imageUrl: List<String> = listOf(""),
        var productUrl: List<String> = listOf("")): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.createStringArrayList())

    override fun equals(other: Any?): Boolean {
        if(this == other) return true
        if(other == null || other !is Banner) return false
        if (this::class !== other::class) return false

        if(title != other.title) return false
        if(subscript != other.subscript) return false
        if(imageUrl.size != other.imageUrl.size) return false
        if(imageUrl != other.imageUrl) return false
        if(productUrl.size != other.productUrl.size) return false
        if(productUrl != other.productUrl) return false

        return true
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(subscript)
        parcel.writeStringList(imageUrl)
        parcel.writeStringList(productUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Banner> {
        override fun createFromParcel(parcel: Parcel): Banner {
            return Banner(parcel)
        }

        override fun newArray(size: Int): Array<Banner?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Title=" + title +
                "\nSubscript=" + subscript +
                "\nImageURL=" + imageUrl +
                "\nProductUrl=" + productUrl
    }
}