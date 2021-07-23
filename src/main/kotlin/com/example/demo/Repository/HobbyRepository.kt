package com.example.demo.Repository

import com.example.demo.Entity.HobbyEntity
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
class HobbyRepository(
    @PersistenceContext val entityManager: EntityManager,
    val hobbyRepositoryInterface: HobbyRepositoryInterface
) {

    @Transactional
    fun insertHobby(complexity: Int, hobby_name: String, person_id: Long) {
        entityManager.createNativeQuery("INSERT INTO hobby (complexity, hobby_name, person_id) VALUES (?, ?, ?)")
            .setParameter(1, complexity)
            .setParameter(2, hobby_name)
            .setParameter(3, person_id)
            .executeUpdate()
    }

    @Transactional
    fun deleteHobby(complexity: Int, hobby_name: String, person_id: Long) {
        entityManager.createNativeQuery("DELETE FROM hobby WHERE complexity = ? and lower(hobby_name) like lower(?) and person_id = ?")
            .setParameter(1, complexity)
            .setParameter(2, hobby_name)
            .setParameter(3, person_id)
            .executeUpdate()
    }

    fun getHobbiesByPerson(id: Long): MutableList<HobbyEntity> {
        return hobbyRepositoryInterface.getHobbiesByPerson(id)
    }
}