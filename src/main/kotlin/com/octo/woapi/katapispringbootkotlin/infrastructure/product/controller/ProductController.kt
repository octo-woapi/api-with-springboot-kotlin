package com.octo.woapi.katapispringbootkotlin.infrastructure.product.controller

import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import com.octo.woapi.katapispringbootkotlin.domain.product.ProductSortAttributes
import com.octo.woapi.katapispringbootkotlin.domain.product.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/products")
class ProductController(val productService: ProductService) {

    companion object {
        const val DEFAULT_PAGE: Int = 0
        const val DEFAULT_PER_PAGE: Int = 5
        const val MAX_PER_PAGE: Int = 10
    }

    /*************************************************************************
     * GET methods
     *************************************************************************/

    @GetMapping(value = [""], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun listAllProducts(): List<Product> {
        return productService.getAllProducts()
    }

    @GetMapping(value = ["/{productId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getProductById(@PathVariable productId: Long): Product {
        return productService.getProductById(productId)
    }

    /*************************************************************************
     * GET methods with sort & range
     *************************************************************************/

    @GetMapping(value = [""], produces = [MediaType.APPLICATION_JSON_VALUE], params = ["sort"])
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    fun listAllProductsSorted(
        @RequestParam(
            value = "sort",
            required = false
        ) sortParam: String?
    ): List<Product?> {
        return productService.getSortedAndPaginatedProducts(
            ProductSortAttributes.get(sortParam),
            DEFAULT_PAGE,
            DEFAULT_PER_PAGE
        )
    }

    @GetMapping(value = [""], produces = [MediaType.APPLICATION_JSON_VALUE], params = ["page", "per-page"])
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    fun listAllProductsPaginated(
        @RequestParam(value = "page", required = true) page: Int,
        @RequestParam(value = "per-page", required = true) perPage: Int,
        response: HttpServletResponse
    ): List<Product> {
        val limitedPerPage = if (perPage > MAX_PER_PAGE) MAX_PER_PAGE else perPage
        return productService.getSortedAndPaginatedProducts(ProductSortAttributes.ID, page, limitedPerPage)
    }

    @GetMapping(value = [""], produces = [MediaType.APPLICATION_JSON_VALUE], params = ["sort", "page", "per-page"])
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    fun listAllProductsSortedAndPaginated(
        @RequestParam(value = "sort", required = true) sortParam: String?,
        @RequestParam(value = "page", required = true) page: Int,
        @RequestParam(value = "per-page", required = true) perPage: Int,
        response: HttpServletResponse
    ): List<Product>? {
        val limitedPerPage = if (perPage > MAX_PER_PAGE) MAX_PER_PAGE else perPage
        return productService.getSortedAndPaginatedProducts(ProductSortAttributes.get(sortParam), page, limitedPerPage)
    }

    /*************************************************************************
     * POST methods
     *************************************************************************/

    @PostMapping(value = [""], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody product: Product): Product {
        return productService.createProduct(product)
    }

    /*************************************************************************
     * DELETE methods
     *************************************************************************/

    @DeleteMapping(value = ["/{productId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProductById(@PathVariable productId: Long?) {
        productService.deleteProduct(productId!!)
    }
}