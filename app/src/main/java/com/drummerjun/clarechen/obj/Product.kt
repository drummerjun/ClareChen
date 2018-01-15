package com.drummerjun.clarechen.obj

/**
 * Created by drummerjun on 1/13/2018.
 */
data class Product(
        var cate: Int = 0,
        var color: String = "",
        var image: List<String> = listOf(""),
        var link: List<String> = listOf(""),
        var name: List<String> = listOf(""),
        var price: List<String> = listOf("") ) {
    override fun equals(other: Any?): Boolean {
        if(this == other) return true
        if(other == null || other !is Product) return false
        if (this::class !== other::class) return false

        if(cate != other.cate) return false
        if(!color.equals(other.color)) return false
        if(image.size != other.image.size) return false
        if(!image.equals(other.image)) return false
        if(link.size != other.link.size) return false
        if(!link.equals(other.link)) return false
        if(name.size != other.name.size) return false
        if(!name.equals(other.name)) return false
        if(price.size != other.price.size) return false
        if(!price.equals(other.price)) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}