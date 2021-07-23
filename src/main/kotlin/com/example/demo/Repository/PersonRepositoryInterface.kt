package com.example.demo.Repository

import com.example.demo.Entity.PersonEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface PersonRepositoryInterface: JpaRepository<PersonEntity, Long> {
    @Query("SELECT person_id FROM persons WHERE lower(name) like lower(?1) and lower(birthday) like lower(?2)", nativeQuery = true)
    fun getId(name: String, birthday: String): Long
}