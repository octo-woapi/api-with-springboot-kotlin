package com.octo.woapi.katapispringbootkotlin.infrastructure.persistence

import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "products")
data class ProductDB(
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)  var id: Long? = null,
    var name: String,
    var price: BigDecimal,
    var weight: BigDecimal
) {
   fun toProduct(): Product {
       return Product(id = id!!, name = name, price = price, weight = weight)
   }
}
