package com.octo.woapi.katapispringbootkotlin.infrastructure.controller

import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import com.octo.woapi.katapispringbootkotlin.domain.product.ProductSortAttributes
import com.octo.woapi.katapispringbootkotlin.infrastructure.persistence.ProductService
import com.octo.woapi.katapispringbootkotlin.infrastructure.rest.ProductResource
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*
import java.util.stream.Collectors
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

    @GetMapping(value = [""], produces = [MediaTypes.HAL_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun listAllProductsHypermedia(): CollectionModel<ProductResource> {
        val productResources: List<ProductResource> = productService.getAllProducts()
            .stream()
            .map { ProductResource(it) }
            .collect(Collectors.toList())
        return CollectionModel.of(productResources)
    }

    @GetMapping(value = [""], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun listAllProducts(): List<Product> {
        return productService.getAllProducts()
    }

    @GetMapping(value = ["/{productId}"], produces = [MediaTypes.HAL_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun getProductById(@PathVariable productId: Long): EntityModel<ProductResource> {
        val product = productService.getProductById(productId)
        return EntityModel.of(ProductResource(product))
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

    @PostMapping(value = [""], produces = [MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE])
    fun createProduct(@RequestBody product: Product): ResponseEntity<Product> {
        val createdProduct = productService.createProduct(product.name, product.price, product.weight)
        val linkToThisProduct: Optional<Link> = ProductResource(createdProduct).getLink("self")

        if (linkToThisProduct.isPresent) {
            return created(URI.create(linkToThisProduct.get().href)).body(createdProduct)
        }
        return status(HttpStatus.INTERNAL_SERVER_ERROR).build()
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