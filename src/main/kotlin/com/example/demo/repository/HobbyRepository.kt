package com.example.demo.repository

import com.example.demo.entity.HobbyEntity
import org.springframework.data.jpa.repository.JpaRepository

interface HobbyRepository: JpaRepository<HobbyEntity, Long> {
    fun findAllByPerson(person_id: Long): MutableList<HobbyEntity>
}