package com.example.demo.repository

import com.example.demo.entity.Person
import org.springframework.data.jpa.repository.JpaRepository

interface PersonRepository : JpaRepository<Person, Long> {
    override fun findAll(): MutableList<Person>
    fun findAllByNameLikeAndBirthdayLike(name: String, birthday: String): MutableList<Person>
}