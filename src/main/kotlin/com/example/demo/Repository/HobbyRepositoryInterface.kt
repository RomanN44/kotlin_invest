package com.example.demo.Repository

import com.example.demo.Entity.HobbyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface HobbyRepositoryInterface: JpaRepository<HobbyEntity, Long> {
    @Query("SELECT * FROM hobby WHERE person_id = ?1", nativeQuery = true)
    fun findAllByP(id: Long): MutableList<HobbyEntity>
}
