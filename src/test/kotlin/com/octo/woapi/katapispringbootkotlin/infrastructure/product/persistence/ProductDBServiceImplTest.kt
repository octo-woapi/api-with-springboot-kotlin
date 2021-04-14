package com.octo.woapi.katapispringbootkotlin.infrastructure.product.persistence

import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import com.octo.woapi.katapispringbootkotlin.infrastructure.product.exception.ProductNotFoundException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class ProductDBServiceImplTest(@Autowired val productDBServiceImpl: ProductDBServiceImpl) {

    @Test
    fun `getAllProducts should return 12 products`() {
        //when
        val products = productDBServiceImpl.getAllProducts()
        //then
        Assertions.assertThat(products).isNotNull.hasSize(12)
    }

    @Test
    fun `getProductById should return a correct product when giving a correct id`() {
        //when
        val product = productDBServiceImpl.getProductById(2)
        //then
        Assertions.assertThat(product).isNotNull
            .hasFieldOrPropertyWithValue("id", 2.toLong())
            .hasFieldOrPropertyWithValue("name", "Cement bag 25kg")
            .hasFieldOrPropertyWithValue("price", 15.0.toBigDecimal())
            .hasFieldOrPropertyWithValue("weight", 25.0.toBigDecimal())
    }

    @Test
    fun `getProductById should throw an exception with giving an incorrect id`() {
        //when-then
        Assertions.assertThatThrownBy { productDBServiceImpl.getProductById(999) }
            .isInstanceOf(ProductNotFoundException::class.java)
    }

    @Test
    fun `deleteProductById should delete a product with a correct id`() {
        //when
        productDBServiceImpl.deleteProduct(1)
        val products = productDBServiceImpl.getAllProducts()
        //then
        Assertions.assertThat(products).hasSize(11)
    }

    @Test
    fun `deleteProductById should delete 0 product when giving an incorrect id`() {
        //when-then
        Assertions.assertThatThrownBy { productDBServiceImpl.deleteProduct(999) }
            .isInstanceOf(ProductNotFoundException::class.java)
    }

    @Test
    fun `insertProduct should add a new product`() {
        //when
        val newProduct =
            Product(
                id = null,
                name = "Screw box 100mm 50 units",
                price = 50.0.toBigDecimal(),
                weight = 25.0.toBigDecimal()
            )
        val insertedProduct = productDBServiceImpl.createProduct(newProduct)
        val products = productDBServiceImpl.getAllProducts()
        //then
        Assertions.assertThat(products).hasSize(13)
        Assertions.assertThat(insertedProduct.id).isNotNull;
        Assertions.assertThat(products[12].weight).isEqualTo(25.0.toBigDecimal())
        Assertions.assertThat(products[12].price).isEqualTo(50.0.toBigDecimal())
        Assertions.assertThat(products[12].name).isEqualTo("Screw box 100mm 50 units")
    }
}