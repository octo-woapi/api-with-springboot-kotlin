package com.octo.woapi.katapispringbootkotlin.infrastructure.persistence;

import com.octo.woapi.katapispringbootkotlin.domain.product.Product;
import com.octo.woapi.katapispringbootkotlin.domain.product.ProductSortAttributes

import java.math.BigDecimal;

interface ProductService {

    fun getAllProducts(): List<Product>

    fun getProductById(id: Long): Product

    fun createProduct(name: String, price: BigDecimal, weight: BigDecimal): Product

    fun deleteProduct(id: Long): Long

    fun getSortedAndPaginatedProducts(sortField: ProductSortAttributes, page: Int, perPage: Int): List<Product>
}
