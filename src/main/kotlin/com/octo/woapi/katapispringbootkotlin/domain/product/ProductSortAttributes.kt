package com.octo.woapi.katapispringbootkotlin.domain.product;

import java.util.*

enum class ProductSortAttributes(val attributeLowerCase: String, val comparator: Comparator<Product>) {

    ID("id", Comparator.comparing(Product::id)),
    NAME("name", Comparator.comparing(Product::name)),
    PRICE("price", Comparator.comparing(Product::price)),
    WEIGHT("weight", Comparator.comparing(Product::weight));

    companion object {
        fun contains(value: String?): Boolean {
            for (attribute: ProductSortAttributes in values()) {
                if (value != null && attribute.attributeLowerCase == value.toLowerCase()) {
                    return true
                }
            }
            return false
        }

        fun get(value: String ?): ProductSortAttributes {
            val sortExist = contains(value)
            return if (sortExist && value != null) valueOf(value.toUpperCase()) else ID
        }
    }
}
