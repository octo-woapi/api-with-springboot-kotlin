package com.octo.woapi.katapispringbootkotlin.infrastructure.order.controller;

import com.octo.woapi.katapispringbootkotlin.domain.order.Order
import com.octo.woapi.katapispringbootkotlin.domain.order.OrderService;
import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController(val orderService: OrderService) {

    /*************************************************************************
     * GET methods
     *************************************************************************/

    @GetMapping(value = [""], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun listAllProducts(): List<Order> {
        return orderService.getAllOrders()
    }

    @GetMapping(value = ["/{orderId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getOrderById(@PathVariable orderId: Long): Order {
        return orderService.getOrderById(orderId)
    }

}
