package com.octo.woapi.katapispringbootkotlin.infrastructure.rest

import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import com.octo.woapi.katapispringbootkotlin.infrastructure.controller.ProductController
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn

@Relation(collectionRelation = "productResources")
data class ProductResource(val product: Product) : RepresentationModel<ProductResource>() {

    init {
        this.add(methodOn(ProductController::class.java).getProductById(product.id).let { linkTo(it).withSelfRel() })
    }
}