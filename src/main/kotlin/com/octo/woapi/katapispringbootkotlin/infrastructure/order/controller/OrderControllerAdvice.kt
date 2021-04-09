package com.octo.woapi.katapispringbootkotlin.infrastructure.order.controller

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.octo.woapi.katapispringbootkotlin.infrastructure.order.exception.OrderNotFoundException
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice(assignableTypes = [OrderController::class])
class OrderControllerAdvice {

    @ResponseBody
    @ExceptionHandler(OrderNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun orderNotFoundExceptionHandler(ex: OrderNotFoundException): Problem {
        return Problem.create()
            .withTitle("OrderNotFound")
            .withDetail(ex.message)
            .withStatus(HttpStatus.NOT_FOUND)
    }

    @ResponseBody
    @ExceptionHandler(value = [MissingKotlinParameterException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMissingKotlinParameter(e: MissingKotlinParameterException): Problem {
        return Problem.create()
            .withDetail("param '${e.parameter.name}' is required")
            .withStatus(HttpStatus.BAD_REQUEST);
    }
}