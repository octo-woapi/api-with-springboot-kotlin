package com.octo.woapi.katapispringbootkotlin.domain.order

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import java.math.BigDecimal
import kotlin.math.floor

data class Order(
    val id: Long?,
    val status: OrderStatus = OrderStatus.PENDING,
    val products: List<Product>
) {

    @JsonIgnore
    private var discountThreshold: Double = 1000.0
    @JsonIgnore
    private val weightFeesThreshold: Int = 10
    @JsonIgnore
    private val weightFees = 25

    @JsonProperty("totalAmount")
    fun getTotalAmount(): BigDecimal {
        val total = getSumOfAllProductAmount() + getShipmentAmount()
        return if (total > discountThreshold.toBigDecimal()) total.multiply(0.95.toBigDecimal()) else total
    }

    @JsonIgnore
    fun getSumOfAllProductAmount(): BigDecimal {
        return products.sumOf { it.price }
    }

    @JsonProperty("shipmentAmount")
    fun getShipmentAmount(): BigDecimal {
        return BigDecimal(floor(getTotalWeight().toDouble() / weightFeesThreshold) * weightFees)
    }

    @JsonProperty("totalWeight")
    fun getTotalWeight(): BigDecimal {
        return products.sumOf { it.weight }
    }
}