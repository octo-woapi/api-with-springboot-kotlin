package com.octo.woapi.katapispringbootkotlin.infrastructure.order.persistence

import com.octo.woapi.katapispringbootkotlin.domain.order.Order
import com.octo.woapi.katapispringbootkotlin.domain.order.OrderStatus
import com.octo.woapi.katapispringbootkotlin.infrastructure.product.persistence.ProductDB
import javax.persistence.*

@Entity
@Table(name = "orders")
data class OrderDB(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    var status: String,
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(name = "orders_content", joinColumns = [JoinColumn(name = "order_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")])
    val productsDB: List<ProductDB> = emptyList()
) {
    fun toOrder(): Order {
        val products = productsDB.map { it.toProduct() }
        return Order(id, OrderStatus.valueOf(status), products)
    }
}