package com.octo.woapi.katapispringbootkotlin.infrastructure.persistence

import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import com.octo.woapi.katapispringbootkotlin.infrastructure.exception.ProductNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class ProductDaoTest(@Autowired val productDao: ProductDao) {

    @Test
    fun `getAllProducts should return 12 products`() {
        //when
        val products = productDao.getAllProducts()
        //then
        assertThat(products).isNotNull.hasSize(12)
    }

    @Test
    fun `getProductById should return a correct product when giving a correct id `() {
        //when
        val product = productDao.getProductById(2)
        //then
        assertThat(product).isNotNull
            .hasFieldOrPropertyWithValue("id", 2.toLong())
            .hasFieldOrPropertyWithValue("name", "Cement bag 25kg")
            .hasFieldOrPropertyWithValue("price", 15.0.toBigDecimal())
            .hasFieldOrPropertyWithValue("weight", 25.0.toBigDecimal())
    }

    @Test
    fun `getProductById should throw an exception with giving an incorrect id `() {
        //when-then
        assertThatThrownBy { productDao.getProductById(999) }.isInstanceOf(ProductNotFoundException::class.java)
    }

    @Test
    fun `deleteProductById should delete a product with a correct id`() {
        //when
        val isDeleted = productDao.deleteProductById(1)
        val products = productDao.getAllProducts()
        //then
        assertThat(isDeleted).isEqualTo(1)
        assertThat(products).hasSize(11)
    }

    @Test
    fun `deleteProductById should delete 0 product when giving an incorrect id`() {
        //when
        val isDeleted = productDao.deleteProductById(999)
        val products = productDao.getAllProducts()
        //then
        assertThat(isDeleted).isEqualTo(0)
        assertThat(products).hasSize(12);
    }

    @Test
    fun `insertProduct should add a product with id 13`() {
        //when
        val newProduct = Product(name = "Screw box 100mm 50 units", price = 50.0.toBigDecimal(), weight = 25.0.toBigDecimal())
        val newId = productDao.insertProduct(newProduct)
        val products = productDao.getAllProducts()
        //then
        assertThat(products).hasSize(13)
        assertThat(newId).isEqualTo(13)
        assertThat(products[12].weight).isEqualTo(25.0.toBigDecimal())
        assertThat(products[12].price).isEqualTo(50.0.toBigDecimal())
        assertThat(products[12].name).isEqualTo("Screw box 100mm 50 units")
    }
}