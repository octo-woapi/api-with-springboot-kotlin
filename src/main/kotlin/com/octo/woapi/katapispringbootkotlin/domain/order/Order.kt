package com.octo.woapi.katapispringbootkotlin.domain.order

import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import java.math.BigDecimal
import kotlin.math.floor

data class Order(
    val id: Long?,
    val status: OrderStatus = OrderStatus.PENDING,
    val products: List<Product>
) {

    private var discountThreshold: Double = 1000.0
    private val weightFeesThreshold: Int = 10
    private val weightFees = 25

    fun getTotalAmount(): BigDecimal {
        val total = getSumOfAllProductAmount() + getShipmentAmount()
        return if (total > discountThreshold.toBigDecimal()) total.multiply(0.95.toBigDecimal()) else total
    }

    fun getSumOfAllProductAmount(): BigDecimal {
        return products.sumOf { it.price }
    }

    fun getShipmentAmount(): BigDecimal {
        return BigDecimal(floor(getTotalWeight().toDouble() / weightFeesThreshold) * weightFees)
    }

    fun getTotalWeight(): BigDecimal {
        return products.sumOf { it.weight }
    }
}