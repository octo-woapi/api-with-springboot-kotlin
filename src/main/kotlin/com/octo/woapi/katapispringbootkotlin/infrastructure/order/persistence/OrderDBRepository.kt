package com.octo.woapi.katapispringbootkotlin.infrastructure.order.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface OrderDBRepository: JpaRepository<OrderDB, Long> {
}