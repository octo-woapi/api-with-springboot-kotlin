package com.octo.woapi.katapispringbootkotlin.infrastructure.order.persistence

import com.octo.woapi.katapispringbootkotlin.domain.order.OrderStatus
import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import com.octo.woapi.katapispringbootkotlin.infrastructure.order.exception.OrderNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class OrderDBServiceImplTest(@Autowired val orderDBServiceImpl: OrderDBServiceImpl) {

    @Test
    fun `getAllOrders should return 2 orders`() {
        //when
        val orders = orderDBServiceImpl.getAllOrders()
        //then
        assertThat(orders).isNotNull.hasSize(2)
    }

    @Test
    fun `getOrderById should return a correct order when giving a correct id`() {
        //when
        val anOrder = orderDBServiceImpl.getOrderById(1)
        //then
        assertThat(anOrder).isNotNull
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("status", OrderStatus.PENDING)
        assertThat(anOrder.products).isNotNull.hasSize(1)
    }

    @Test
    fun `getOrderById should throw an exception with giving an incorrect id`() {
        //when-then
        assertThatThrownBy { orderDBServiceImpl.getOrderById(999) }
            .isInstanceOf(OrderNotFoundException::class.java)
    }

    @Test
    fun `deleteOrderById should delete a order with a correct id`() {
        //when
        orderDBServiceImpl.deleteOrder(1)
        val orders = orderDBServiceImpl.getAllOrders()
        //then
        assertThat(orders).hasSize(1)
    }

    @Test
    fun `deleteOrderById should delete 0 order, throw an exception when giving an incorrect id`() {
        //when-then
        assertThatThrownBy { orderDBServiceImpl.deleteOrder(999) }
            .isInstanceOf(OrderNotFoundException::class.java)
    }

    @Test
    fun `createOrder should add a new order`() {
        //when
        val aKnife = Product(id = null, name = "knife", price = 25.0.toBigDecimal(), weight = 3.0.toBigDecimal())
        val aDesk = Product(id = null, name = "desk", price = 100.0.toBigDecimal(), weight = 20.0.toBigDecimal())
        val insertedOrder = orderDBServiceImpl.createOrder(listOf(aDesk, aKnife))
        val orders = orderDBServiceImpl.getAllOrders()
        //then
        assertThat(orders).hasSize(3)
        assertThat(insertedOrder.id).isNotNull
        assertThat(insertedOrder.status).isEqualTo(OrderStatus.PENDING)
        assertThat(insertedOrder.products).hasSize(2)
    }
}