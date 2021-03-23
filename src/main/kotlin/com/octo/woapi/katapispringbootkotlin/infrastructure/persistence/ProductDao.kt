package com.octo.woapi.katapispringbootkotlin.infrastructure.persistence

import com.octo.woapi.katapispringbootkotlin.domain.product.Product
import com.octo.woapi.katapispringbootkotlin.infrastructure.exception.ProductNotFoundException
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class ProductDao(val jdbc: JdbcTemplate) {

    val productRowMapper: RowMapper<Product> = RowMapper { rs: ResultSet, _: Int ->
        Product(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getBigDecimal("price"),
            rs.getBigDecimal("weight")
        )
    }

    fun getAllProducts(): List<Product> {
        return jdbc.query("SELECT * FROM Products", productRowMapper)
    }

    fun getProductById(id: Long): Product? {
        try {
            return jdbc.queryForObject("SELECT * FROM Products WHERE id = ?", productRowMapper, id)
        } catch(e: IncorrectResultSizeDataAccessException) {
            throw ProductNotFoundException(id)
        }
    }

    fun deleteProductById(id: Long): Long {
        return jdbc.update("DELETE FROM Products WHERE id = ?", id).toLong()
    }

    fun insertProduct(product: Product): Long {
        val keyHolder = GeneratedKeyHolder()
        jdbc.update({ con ->
            val ps = con.prepareStatement("INSERT INTO Products(name, price, weight) VALUES (?, ?, ?)", arrayOf("id"))
            ps.setString(1, product.name)
            ps.setBigDecimal(2, product.price)
            ps.setBigDecimal(3, product.weight)
            return@update ps
        }, keyHolder)
        return keyHolder.key?.toLong()!!
    }
}
