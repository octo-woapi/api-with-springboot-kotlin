package com.octo.woapi.katapispringbootkotlin.infrastructure.exception

class ProductNotFoundException(id: Long) : RuntimeException("could not find product with id : $id")