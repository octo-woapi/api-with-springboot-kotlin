package com.octo.woapi.katapispringbootkotlin.domain.order

import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OrderTest {

    @Test
    fun `should return the sum of all weight products`() {
        //given
        val aKnife = Product(id = null, name = "knife", price = 100.0.toBigDecimal(), weight = 3.0.toBigDecimal())
        val aDesk = Product(id = null, name = "desk", price = 100.0.toBigDecimal(), weight = 20.0.toBigDecimal())
        val anOrder = Order(id = null, products = listOf(aKnife, aDesk))
        //when
        val totalWeight = anOrder.getTotalWeight()
        //then
        assertThat(totalWeight).isEqualTo(23.0.toBigDecimal())
    }

    @Test
    fun `should return 0kg when no product in order`() {
        //given
        val anOrder = Order(id = null, products = listOf())
        //when
        val totalWeight = anOrder.getTotalWeight()
        //then
        assertThat(totalWeight).isEqualTo(0.toBigDecimal())
    }

    @Test
    fun `should return 0€ of total shipment fees for a total weight inferior to 10kg`() {
        //given
        val product01 = Product(id = null, name = "knife", price = 100.0.toBigDecimal(), weight = 3.0.toBigDecimal())
        val orderTest = Order(id = null, products = listOf(product01))
        //when
        val totalShipmentAmount = orderTest.getShipmentAmount()
        //then
        assertThat(totalShipmentAmount).isEqualTo(0.toBigDecimal())
    }

    @Test
    fun `should return 25€ more of shipment fees for every 10kg more`() {
        //given
        val aKnife = Product(id = null, name = "knife", price = 25.0.toBigDecimal(), weight = 3.0.toBigDecimal())
        val aDesk = Product(id = null, name = "desk", price = 100.0.toBigDecimal(), weight = 20.0.toBigDecimal())
        val orderTest = Order(id = null, products = listOf(aDesk, aKnife))
        //when
        val totalShipmentAmount = orderTest.getShipmentAmount()
        //then
        assertThat(totalShipmentAmount).isEqualTo(50.toBigDecimal())
    }

    @Test
    fun `should return 0€ (sum of all prices) when no product in order`() {
        //given
        val orderTest = Order(id = null, products = listOf())
        //when
        val sumOfProductAmount = orderTest.getSumOfAllProductAmount()
        //then
        assertThat(sumOfProductAmount).isEqualTo(0.toBigDecimal())
    }

    @Test
    fun `should return the sum of all prices of all products`() {
        //given
        val aKnife = Product(id = null, name = "knife", price = 25.0.toBigDecimal(), weight = 3.0.toBigDecimal())
        val aDesk = Product(id = null, name = "desk", price = 100.0.toBigDecimal(), weight = 20.0.toBigDecimal())
        val orderTest = Order(id = null, products = listOf(aDesk, aKnife))
        //when
        val sumOfProductAmount = orderTest.getSumOfAllProductAmount()
        //then
        assertThat(sumOfProductAmount).isEqualTo(125.0.toBigDecimal())
    }

    @Test
    fun `should return the total price with no discount when total price inferior to 1000`() {
        //given
        val aKnife = Product(id = null, name = "knife", price = 25.0.toBigDecimal(), weight = 3.0.toBigDecimal())
        val aDesk = Product(id = null, name = "desk", price = 100.0.toBigDecimal(), weight = 20.0.toBigDecimal())
        val orderTest = Order(id = null, products = listOf(aDesk, aKnife))
        //when
        val totalAmount = orderTest.getTotalAmount()
        //then
        assertThat(totalAmount).isEqualTo(175.0.toBigDecimal())
    }

    @Test
    fun `should return the total price with a 5% discount when total price superior to 1000`() {
        //given
        val aOldJar = Product(id = null, name = "old jar", price = 1500.toBigDecimal(), weight = 5.toBigDecimal())
        val aPainting = Product(id = null, name = "painting", price = 250.toBigDecimal(), weight = 2.toBigDecimal())
        val orderTest = Order(id = null, products = listOf(aOldJar, aPainting))
        //when
        val totalAmount = orderTest.getTotalAmount()
        //then
        assertThat(totalAmount).isEqualTo(BigDecimal("1662.50"))
    }
}