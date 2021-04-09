package com.octo.woapi.katapispringbootkotlin.infrastructure.order.persistence

import com.octo.woapi.katapispringbootkotlin.domain.order.Order
import com.octo.woapi.katapispringbootkotlin.domain.order.OrderService
import com.octo.woapi.katapispringbootkotlin.domain.order.OrderStatus
import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import com.octo.woapi.katapispringbootkotlin.infrastructure.order.exception.OrderNotFoundException
import com.octo.woapi.katapispringbootkotlin.infrastructure.product.persistence.ProductDB
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Component

@Component
class OrderDBServiceImpl(val orderDBRepository: OrderDBRepository) : OrderService {

    override fun getAllOrders(): List<Order> {
        return orderDBRepository.findAll()
            .map { orderDB -> orderDB.toOrder() }
            .toList()
    }

    override fun getOrderById(id: Long): Order {
        return orderDBRepository.findById(id)
            .orElseThrow { OrderNotFoundException(id) }
            .toOrder()
    }

    override fun createOrder(products: List<Product>): Order {
        val productsDB = products.map { ProductDB(name = it.name, price = it.price, weight = it.weight) }
        val orderDB = OrderDB(status = OrderStatus.PENDING.toString(), productsDB = productsDB)
        orderDBRepository.save(orderDB)
        return orderDB.toOrder()
    }

    override fun deleteOrder(id: Long) {
        try {
            orderDBRepository.deleteById(id)
        } catch (e: EmptyResultDataAccessException) {
            throw OrderNotFoundException(id)
        }
    }
}