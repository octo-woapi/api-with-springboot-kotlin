package com.octo.woapi.katapispringbootkotlin.domain.product

interface ProductService {

    fun getAllProducts(): List<Product>

    fun getProductById(id: Long): Product

    fun createProduct(product: Product): Product

    fun deleteProduct(id: Long)

    fun getSortedAndPaginatedProducts(sortField: ProductSortAttributes, page: Int, perPage: Int): List<Product>
}
