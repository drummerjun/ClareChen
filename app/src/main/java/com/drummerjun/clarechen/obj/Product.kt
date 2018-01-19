package com.drummerjun.clarechen.obj

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by drummerjun on 1/13/2018.
 */

class Product(
        var cate: Int = 0,
        var color: String = "",
        var image: List<String> = listOf(""),
        var link: List<String> = listOf(""),
        var name: List<String> = listOf(""),
        var price: List<String> = listOf("") ): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.createStringArrayList(),
            parcel.createStringArrayList(),
            parcel.createStringArrayList())

    override fun equals(other: Any?): Boolean {
        if(this == other) return true
        if(other == null || other !is Product) return false
        if (this::class !== other::class) return false

        if(cate != other.cate) return false
        if(color != other.color) return false
        if(image.size != other.image.size) return false
        if(image != other.image) return false
        if(link.size != other.link.size) return false
        if(link != other.link) return false
        if(name.size != other.name.size) return false
        if(name != other.name) return false
        if(price.size != other.price.size) return false
        if(price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(cate)
        parcel.writeString(color)
        parcel.writeStringList(image)
        parcel.writeStringList(link)
        parcel.writeStringList(name)
        parcel.writeStringList(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        var images = ""
        for(i in image.indices) {
            images += "[" + i + "]" + image[i] + "\n"
        }

        var links = ""
        for(i in link.indices) {
            links += "[" + i + "]" + link[i] + "\n"
        }

        var prices = ""
        for(i in price.indices) {
            prices += "[" + i + "]" + price[i] + "\n"
        }
        return "Name=" + name + ";\nCategory=" + cate + "; Color=" + color +
                ";\nPrice=" + price + ";\nImages=" + image + ";\nLinks=" + link
    }
}