package com.example.demo.repository

import com.example.demo.entity.Hobby
import org.springframework.data.jpa.repository.JpaRepository

interface HobbyRepository: JpaRepository<Hobby, Long> {
    fun findAllByPerson(person_id: Long): MutableList<Hobby>
}