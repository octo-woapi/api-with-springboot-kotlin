package com.octo.woapi.katapispringbootkotlin.infrastructure.controller

import com.octo.woapi.katapispringbootkotlin.infrastructure.exception.ProductNotFoundException
import com.octo.woapi.katapispringbootkotlin.infrastructure.rest.ProductResource
import org.hamcrest.Matchers
import org.hamcrest.Matchers.hasSize
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Nested
    inner class GetAllProducts {
        @Test
        fun getAllProducts_shouldReturnAllProductsInDB() {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(
                    header()
                        .string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize<Any>(12)))
        }

        @Test
        fun getAllProductsHypermedia_shouldReturnAllProductsInDBInHypermedia() {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
                    .accept(MediaTypes.HAL_JSON)
                    .contentType(MediaTypes.HAL_JSON)
            )
                .andExpect(status().isOk)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(
                    header()
                        .string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)
                )
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.productResources", hasSize<ProductResource>(12)))
        }
    }

    @Nested
    inner class GetProductById {
        @Test
        fun getProductById_shouldReturnTheCorrectProduct() {
            val idToBeTested = 1
            mockMvc.perform(
                MockMvcRequestBuilders.get("/products/$idToBeTested")
                    .accept(MediaTypes.HAL_JSON_VALUE)
                    .contentType(MediaTypes.HAL_JSON_VALUE)
            )
                .andExpect(status().isOk)
                .andExpect(
                    header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)
                )
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.product.id", Matchers.`is`(idToBeTested)))
        }

        @Test
        @Throws(ProductNotFoundException::class)
        fun getProductById_shouldReturn404ifNotFound() {
            val idToBeTested = 999
            mockMvc.perform(
                MockMvcRequestBuilders.get("/products/$idToBeTested")
                    .accept(MediaTypes.HAL_JSON_VALUE)
                    .contentType(MediaTypes.HAL_JSON_VALUE)
            )
                .andExpect(status().is4xxClientError)
                .andExpect(status().`is`(HttpStatus.NOT_FOUND.value()))
                .andExpect(
                    header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)
                )
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(
                    content().string(Matchers.containsString("could not find product with id : $idToBeTested"))
                )
        }
    }

    @Nested
    inner class CreateProduct {
        @Test
        fun createProduct_shouldReturnAProductWithID() {
            //given
            @Language(value = "json")
            val productJson = """
            {
              "name" : "tested",
              "price": 10.10,
              "weight": 12.34
            }            
        """.trimIndent()
            //when-then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/products")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(productJson)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.name", Matchers.`is`("tested")))
                .andExpect(jsonPath("$.price", Matchers.`is`(10.10)))
                .andExpect(jsonPath("$.weight", Matchers.`is`(12.34)))
                .andExpect(jsonPath("$.id", Matchers.`is`(Matchers.notNullValue())))
        }

        @Test
        fun createProduct_shouldReturn400IfNameIsMissing() {
            //given
            @Language(value = "json")
            val productJson = """
            {
              "price": 10.10,
              "weight": 12.34
            }            
        """.trimIndent()
            //when-then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/products")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(productJson)
            )
                .andExpect(status().is4xxClientError)
                .andExpect(jsonPath("$.detail", Matchers.`is`("param 'name' is required")))
        }

        @Test
        fun createProduct_shouldReturn400IfPriceIsMissing() {
            //given
            @Language(value = "json")
            val productJson = """
            {
              "name": "tested",
              "weight": 12.34
            }            
        """.trimIndent()
            //when-then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/products")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(productJson)
            )
                .andExpect(status().is4xxClientError)
                .andExpect(jsonPath("$.detail", Matchers.`is`("param 'price' is required")))
        }

        @Test
        fun createProduct_shouldReturn400IfWeightIsMissing() {
            //given
            @Language(value = "json")
            val productJson = """
            {
              "name": "tested",
              "price": 10.10
            }            
        """.trimIndent()
            //when-then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/products")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(productJson)
            )
                .andExpect(status().is4xxClientError)
                .andExpect(jsonPath("$.detail", Matchers.`is`("param 'weight' is required")))
        }
    }

    @Nested
    inner class DeleteProduct {
        @Test
        fun deleteProduct_shouldReturn204() {
            val idToBeTested = 2
            mockMvc.perform(MockMvcRequestBuilders.delete("/products/$idToBeTested"))
                .andExpect(status().isNoContent)
        }

        @Test
        fun deleteProduct_shouldReturn404IfProductNotFound() {
            val idToBeTested = 5555
            mockMvc.perform(MockMvcRequestBuilders.delete("/products/$idToBeTested"))
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.detail", Matchers.`is`("could not find product with id : $idToBeTested")))
        }
    }

    @Nested
    inner class GetAllProductsWithSort {
        @Test
        fun getAllProducts_shouldSortByNameIfParamSortName() {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/products/?sort=name")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(status().isPartialContent)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize<Int>(5)))
                .andExpect(jsonPath("$.[0].name", Matchers.`is`("Cement bag 25kg")))
                .andExpect(jsonPath("$.[4].name", Matchers.`is`("Concrete reinforcing rod 1 unit")))
        }

        @Test
        fun getAllProducts_shouldSortByWeightIfParamSortWeight() {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/products/?sort=weight")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(status().isPartialContent)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].weight", Matchers.`is`(0.8)))
                .andExpect(jsonPath("$.[4].weight", Matchers.`is`(3.0)))
        }

        @Test
        fun getAllProducts_shouldSortByPriceIfParamSortPrice() {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/products/?sort=price")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(status().isPartialContent)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].price", Matchers.`is`(0.50)))
                .andExpect(jsonPath("$.[4].price", Matchers.`is`(12.0)))
        }

        @Test
        fun getAllProducts_shouldIgnoreWrongSortParam() {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/products/?sort=zezefzefzefzf")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(status().isPartialContent)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].id", Matchers.`is`(1)))
                .andExpect(jsonPath("$.[1].id", Matchers.`is`(2)))
                .andExpect(jsonPath("$.[4].id", Matchers.`is`(5)))
        }

        @Test
        fun getAllProducts_shouldIgnoreEmptySortParam() {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/products/?sort=")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(status().isPartialContent)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].id", Matchers.`is`(1)))
                .andExpect(jsonPath("$.[4].id", Matchers.`is`(5)))
        }
    }

    @Nested
    inner class GetAllProductsPaginated {
        @Test
        fun getAllProductsSorted_shouldReturnProductsFromPage1With3ElementsPerPage() {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/products?page=1&per-page=3")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(status().isPartialContent)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize<Any>(3)))
                .andExpect(jsonPath("$.[0].id", Matchers.`is`(4)))
                .andExpect(jsonPath("$.[2].id", Matchers.`is`(6)))
        }

        @Test
        fun getAllProductsSorted_shouldReturnProductsFromPage0With2ElementsPerPage() {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/products/?page=0&per-page=2")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(status().isPartialContent)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize<Any>(2)))
                .andExpect(jsonPath("$.[0].id", Matchers.`is`(1)))
        }

        @Test
        fun getAllProducts_shouldReturnProductsFromPage1With5ElementsPerPage() {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/products/?page=1&per-page=5")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
            )
                .andExpect(status().isPartialContent)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize<Any>(5)))
                .andExpect(jsonPath("$.[0].id", Matchers.`is`(6)))
                .andExpect(jsonPath("$.[4].id", Matchers.`is`(10)))
        }

        @Test
        fun getAllProductsSorted_shouldlimitPaginationTo10ElementsPerPage() {
            mockMvc.perform(MockMvcRequestBuilders.get("/products/?page=0&per-page=100"))
                .andExpect(jsonPath("$", hasSize<Any>(10)))
        }
    }
}