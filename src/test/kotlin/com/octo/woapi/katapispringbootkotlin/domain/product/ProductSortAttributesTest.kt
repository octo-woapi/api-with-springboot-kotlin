package com.octo.woapi.katapispringbootkotlin.domain.product

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class ProductSortAttributesTest {

    @ParameterizedTest
    @ValueSource(strings = ["name", "NAME"])
    fun `contains should return true if enum exists`(value: String) {
        val contains = ProductSortAttributes.contains(value)
        assertThat(contains).isTrue
    }

    @ParameterizedTest
    @ValueSource(strings = ["wrong", "WRONG"])
    fun `contains should return false if enum doesn't exists`(value: String) {
        val contains = ProductSortAttributes.contains(value)
        assertThat(contains).isFalse
    }

    @ParameterizedTest
    @ValueSource(strings = ["name", "NAME"])
    fun `get should return the enum value if enum exists`(value: String) {
        val productSortName = ProductSortAttributes.get(value)
        assertThat(productSortName).isNotNull
        assertThat(productSortName.attributeLowerCase).isEqualTo("name")
    }

    @ParameterizedTest
    @ValueSource(strings = ["wrong", "WRONG"])
    fun `get should return the ID enum value if enum doesn't exists`(value: String) {
        val productSortName = ProductSortAttributes.get(value)
        assertThat(productSortName).isNotNull
        assertThat(productSortName.attributeLowerCase).isEqualTo("id")
    }
}