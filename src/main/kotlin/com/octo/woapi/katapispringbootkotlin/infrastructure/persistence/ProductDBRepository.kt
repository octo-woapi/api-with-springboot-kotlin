package com.octo.woapi.katapispringbootkotlin.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductDBRepository : JpaRepository<ProductDB, Long> {
}