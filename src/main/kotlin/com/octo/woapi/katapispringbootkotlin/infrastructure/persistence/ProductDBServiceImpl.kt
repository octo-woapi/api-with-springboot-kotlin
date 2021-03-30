package com.octo.woapi.katapispringbootkotlin.infrastructure.persistence

import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import com.octo.woapi.katapispringbootkotlin.domain.product.ProductService
import com.octo.woapi.katapispringbootkotlin.domain.product.ProductSortAttributes
import com.octo.woapi.katapispringbootkotlin.infrastructure.exception.ProductNotFoundException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class ProductDBServiceImpl(val productDBRepository: ProductDBRepository) : ProductService {

    override fun getAllProducts(): List<Product> {
        return productDBRepository.findAll()
            .map { productDB -> productDB.toProduct() }
            .toList()
    }

    override fun getProductById(id: Long): Product {
        return productDBRepository.findById(id)
            .orElseThrow { ProductNotFoundException(id) }
            .toProduct()
    }

    override fun createProduct(product: Product): Product {
        val productDB = ProductDB(name = product.name, price = product.price, weight = product.weight)
        productDBRepository.save(productDB)
        return productDB.toProduct()
    }

    override fun deleteProduct(id: Long) {
        try {
            productDBRepository.deleteById(id)
        } catch (e: EmptyResultDataAccessException) {
            throw ProductNotFoundException(id)
        }
    }

    override fun getSortedAndPaginatedProducts(
        sortField: ProductSortAttributes,
        page: Int,
        perPage: Int
    ): List<Product> {
        val pageRequest = PageRequest.of(page, perPage, Sort.by(sortField.attributeLowerCase))
        return productDBRepository.findAll(pageRequest)
            .map { productDB -> productDB.toProduct() }
            .toList()
    }
}