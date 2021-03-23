package com.octo.woapi.katapispringbootkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.hateoas.config.EnableHypermediaSupport

@SpringBootApplication
@EnableHypermediaSupport(type = [EnableHypermediaSupport.HypermediaType.HAL])
class KatapiSpringbootKotlinApplication

fun main(args: Array<String>) {
    runApplication<KatapiSpringbootKotlinApplication>(*args)
}
