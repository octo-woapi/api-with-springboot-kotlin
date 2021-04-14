package com.octo.woapi.katapispringbootkotlin.infrastructure.order.controller

import com.octo.woapi.katapispringbootkotlin.infrastructure.order.exception.OrderNotFoundException
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasSize
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest(@Autowired val mockMvc: MockMvc) {

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

    @Nested
    inner class CreateOrder {
        @Test
        fun `should return the new order with a new id`() {
            //given
            @Language(value = "json")
            val productsJson = """
            [
                {
                    "name" : "a-product",
                    "price": 10.10,
                    "weight": 5
                },
                {
                    "name" : "another-product",
                    "price": 200.50,
                    "weight": 50
                }
            
            ]          
        """.trimIndent()
            //when-then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(productsJson)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(jsonPath("$.id", `is`(Matchers.notNullValue())))
                .andExpect(jsonPath("$.status", `is`("PENDING")))
                .andExpect(jsonPath("$.products", hasSize<Int>(2)))
                .andExpect(jsonPath("$.totalWeight", `is`(55)))
                .andExpect(jsonPath("$.totalAmount", `is`(335.6)))
                .andExpect(jsonPath("$.shipmentAmount", `is`(125)))
        }

        @Test
        fun `should return http code 400 if no product`() {
            //given
            @Language(value = "json")
            val productJson = """
                []                      
        """.trimIndent()
            //when-then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(productJson)
            )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError)
        }

        @Test
        fun `should return http code 400 if a product is incorrect`() {
            //given
            @Language(value = "json")
            val productJson = """
                [
                    {
                    "price": 10.10,
                    "weight": 12.34
                    } 
                ]                      
        """.trimIndent()
            //when-then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(productJson)
            )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError)
                .andExpect(jsonPath("$.detail", `is`("param 'name' is required")))
        }
    }

    @Nested
    inner class DeleteOrder {
        @Test
        fun `should delete an order and return http code 204`() {
            val idToBeTested = 2
            mockMvc.perform(MockMvcRequestBuilders.delete("/orders/$idToBeTested"))
                .andExpect(MockMvcResultMatchers.status().isNoContent)
        }

        @Test
        fun `should return http code 404 if order not found`() {
            val idToBeTested = 5555
            mockMvc.perform(MockMvcRequestBuilders.delete("/orders/$idToBeTested"))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andExpect(jsonPath("$.detail", `is`("could not find order with id : $idToBeTested")))
        }
    }

}