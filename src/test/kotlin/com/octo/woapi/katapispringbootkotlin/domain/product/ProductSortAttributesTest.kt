package com.octo.woapi.katapispringbootkotlin.domain.product

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class ProductSortAttributesTest {

    @ParameterizedTest
    @ValueSource(strings = ["name", "NAME"])
    fun contains_shouldReturnTrueIfEnumDoesExists(value: String) {
        val contains = ProductSortAttributes.contains(value)
        assertThat(contains).isTrue
    }

    @ParameterizedTest
    @ValueSource(strings = ["wrong", "WRONG"])
    fun contains_shouldReturnFalseIfEnumDoesNotExists(value: String) {
        val contains = ProductSortAttributes.contains(value)
        assertThat(contains).isFalse
    }

    @ParameterizedTest
    @ValueSource(strings = ["name", "NAME"])
    fun get_shouldReturnEnumValueIfValueExists(value: String) {
        val productSortName = ProductSortAttributes.get(value)
        assertThat(productSortName).isNotNull
        assertThat(productSortName.attributeLowerCase).isEqualTo("name")
    }

    @ParameterizedTest
    @ValueSource(strings = ["wrong", "WRONG"])
    fun get_shouldReturnDefaultEnumValueIDIfValueDoesNotExists(value: String) {
        val productSortName = ProductSortAttributes.get(value)
        assertThat(productSortName).isNotNull
        assertThat(productSortName.attributeLowerCase).isEqualTo("id")
    }
}