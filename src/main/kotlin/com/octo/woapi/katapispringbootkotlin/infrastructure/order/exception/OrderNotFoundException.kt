package com.octo.woapi.katapispringbootkotlin.infrastructure.order.exception

class OrderNotFoundException(id: Long) : RuntimeException("could not find order with id : $id")