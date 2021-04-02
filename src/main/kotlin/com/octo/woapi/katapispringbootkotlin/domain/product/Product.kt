package com.octo.woapi.katapispringbootkotlin.domain.product

import java.math.BigDecimal

data class Product (val id: Long?,
                    val name: String,
                    val price: BigDecimal,
                    val weight: BigDecimal)