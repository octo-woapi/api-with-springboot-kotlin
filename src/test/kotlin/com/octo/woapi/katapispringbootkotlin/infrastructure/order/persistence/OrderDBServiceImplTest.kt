package com.octo.woapi.katapispringbootkotlin.infrastructure.order.persistence

import com.octo.woapi.katapispringbootkotlin.domain.order.OrderStatus
import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import com.octo.woapi.katapispringbootkotlin.infrastructure.order.exception.OrderNotFoundException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class OrderDBServiceImplTest(@Autowired val orderDBServiceImpl: OrderDBServiceImpl) {

    @Test
    fun `getAllOrders should return 2 orders`() {
        //when
        val orders = orderDBServiceImpl.getAllOrders()
        //then
        Assertions.assertThat(orders).isNotNull.hasSize(2)
    }

    @Test
    fun `getOrderById should return a correct order when giving a correct id`() {
        //when
        val anOrder = orderDBServiceImpl.getOrderById(1)
        //then
        Assertions.assertThat(anOrder).isNotNull
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("status", OrderStatus.PENDING)
    }

    @Test
    fun `getOrderById should throw an exception with giving an incorrect id`() {
        //when-then
        Assertions.assertThatThrownBy { orderDBServiceImpl.getOrderById(999) }
            .isInstanceOf(OrderNotFoundException::class.java)
    }

    @Test
    fun `deleteOrderById should delete a order with a correct id`() {
        //when
        orderDBServiceImpl.deleteOrder(1)
        val orders = orderDBServiceImpl.getAllOrders()
        //then
        Assertions.assertThat(orders).hasSize(1)
    }

    @Test
    fun `deleteOrderById should delete 0 order, throw an exception when giving an incorrect id`() {
        //when-then
        Assertions.assertThatThrownBy { orderDBServiceImpl.deleteOrder(999) }
            .isInstanceOf(OrderNotFoundException::class.java)
    }

    @Test
    fun `createOrder should add an order with id 3`() {
        //when
        val aKnife = Product(id = null, name = "knife", price = 25.0.toBigDecimal(), weight = 3.0.toBigDecimal())
        val aDesk = Product(id = null, name = "desk", price = 100.0.toBigDecimal(), weight = 20.0.toBigDecimal())
        val insertedOrder = orderDBServiceImpl.createOrder(listOf(aDesk, aKnife))
        val orders = orderDBServiceImpl.getAllOrders()
        //then
        Assertions.assertThat(orders).hasSize(3)
        Assertions.assertThat(insertedOrder.id).isEqualTo(3)
        Assertions.assertThat(orders[2].status).isEqualTo(OrderStatus.PENDING)
    }
}