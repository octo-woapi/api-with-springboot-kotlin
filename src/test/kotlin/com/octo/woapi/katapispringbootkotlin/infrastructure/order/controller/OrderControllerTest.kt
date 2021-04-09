package com.octo.woapi.katapispringbootkotlin.infrastructure.order.controller

import com.octo.woapi.katapispringbootkotlin.infrastructure.order.exception.OrderNotFoundException
import org.hamcrest.Matchers
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Nested
    inner class GetAllOrders {
        @Test
        fun `should return all the orders from the db`() {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/orders")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(
                    MockMvcResultMatchers.header()
                        .string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                )
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Any>(2)))
        }
    }

    @Nested
    inner class GetOrderById {
        @Test
        fun `should return an order from the db with a correct id`() {
            val idToBeTested = 1
            mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/$idToBeTested")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(
                    MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
        }

        @Test
        @Throws(OrderNotFoundException::class)
        fun `should return a http code 404 if order not found`() {
            val idToBeTested = 999
            mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/$idToBeTested")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError)
                .andExpect(MockMvcResultMatchers.status().`is`(HttpStatus.NOT_FOUND.value()))
                .andExpect(
                    MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(
                    MockMvcResultMatchers.content()
                        .string(Matchers.containsString("could not find order with id : $idToBeTested"))
                )
        }
    }

}