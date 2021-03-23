package com.octo.woapi.katapispringbootkotlin.domain.product

import java.math.BigDecimal

data class Product (var id: Long = Long.MIN_VALUE,
                    val name: String,
                    val price: BigDecimal,
                    val weight: BigDecimal)