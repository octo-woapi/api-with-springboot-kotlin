package com.octo.woapi.katapispringbootkotlin.infrastructure.persistence

import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import com.octo.woapi.katapispringbootkotlin.domain.product.ProductSortAttributes
import com.octo.woapi.katapispringbootkotlin.infrastructure.exception.ProductNotFoundException
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.stream.Collectors

@Component
class ProductServiceImpl(val productDao: ProductDao) : ProductService {

    override fun getAllProducts(): List<Product> {
        return productDao.getAllProducts();
    }

    override fun getProductById(id: Long): Product {
        return productDao.getProductById(id) ?: throw ProductNotFoundException(id)
    }

    override fun createProduct(name: String, price: BigDecimal, weight: BigDecimal): Product {
        val product = Product(name = name, price = price, weight = weight)
        val generatedId = productDao.insertProduct(product)
        product.id = generatedId
        return product
    }

    override fun deleteProduct(id: Long): Long {
        val deletedId = productDao.deleteProductById(id)
        return if (deletedId != 0L) deletedId else throw ProductNotFoundException(id)
    }

    override fun getSortedAndPaginatedProducts(
        sortField: ProductSortAttributes,
        page: Int,
        perPage: Int
    ): List<Product> {
        val sortedProducts: List<Product> = getSortedProducts(sortField)
        return paginateProducts(sortedProducts, page, perPage)
    }

    fun paginateProducts(products: List<Product>, page: Int, perPage: Int): List<Product> {
        return try {
            val fromIndex = page * perPage
            products.subList(fromIndex, fromIndex + perPage)
        } catch (e: Exception) {
            products.subList(0, products.size - 1)
        }
    }

    private fun getSortedProducts(sortField: ProductSortAttributes): List<Product> {
        return getAllProducts()
            .stream()
            .sorted(sortField.comparator)
            .collect(Collectors.toList());
    }
}