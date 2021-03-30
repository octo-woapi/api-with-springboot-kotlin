package com.octo.woapi.katapispringbootkotlin.domain.product

enum class ProductSortAttributes(val attributeLowerCase: String) {

    ID("id"),
    NAME("name"),
    PRICE("price"),
    WEIGHT("weight");

    companion object {
        fun contains(value: String?): Boolean {
            for (attribute: ProductSortAttributes in values()) {
                if (value != null && attribute.attributeLowerCase == value.toLowerCase()) {
                    return true
                }
            }
            return false
        }

        fun get(value: String?): ProductSortAttributes {
            val sortExist = contains(value)
            return if (sortExist && value != null) valueOf(value.toUpperCase()) else ID
        }
    }
}
