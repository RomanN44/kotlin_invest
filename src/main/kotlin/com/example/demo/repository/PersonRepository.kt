package com.example.demo.repository

import com.example.demo.entity.PersonEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PersonRepository : JpaRepository<PersonEntity, Long> {
    override fun findAll(): MutableList<PersonEntity>
    fun findAllByNameLikeAndBirthdayLike(name: String, birthday: String): MutableList<PersonEntity>
}