package com.octo.woapi.katapispringbootkotlin.infrastructure.order.controller;

import com.octo.woapi.katapispringbootkotlin.domain.order.Order
import com.octo.woapi.katapispringbootkotlin.domain.order.OrderService
import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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

    /*************************************************************************
     * POST methods
     *************************************************************************/
    @PostMapping(value = [""], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@RequestBody products: List<Product>): ResponseEntity<Order> {
        if (products.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        orderService.createOrder(products).apply {
            return ResponseEntity.status(HttpStatus.CREATED).body(this)
        }
    }

    /*************************************************************************
     * DELETE methods
     *************************************************************************/

    @DeleteMapping(value = ["/{orderId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOrderById(@PathVariable orderId: Long?) {
        orderService.deleteOrder(orderId!!)
    }

}
