package com.octo.woapi.katapispringbootkotlin.domain.order

import com.octo.woapi.katapispringbootkotlin.domain.product.Product

interface OrderService {

    fun getAllOrders(): List<Order>

    fun getOrderById(id: Long): Order

    fun createOrder(products: List<Product>): Order

    fun deleteOrder(id: Long)
}