package com.example.demo.Repository

import com.example.demo.Entity.PersonEntity
import org.jetbrains.annotations.NotNull
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional


@Repository
class PersonRepository(
    @PersistenceContext val entityManager: EntityManager,
    val personRepositoryInterface: PersonRepositoryInterface
) {

    @Transactional
    @NotNull
    fun getId(name: String, birthday: String): Long {
       return personRepositoryInterface.getId(name, birthday)
    }

    @Transactional
    fun insertPerson(name: String, birthday: String) {
        entityManager.createNativeQuery("INSERT INTO persons (name, birthday) VALUES (?, ?)")
            .setParameter(1, name)
            .setParameter(2, birthday)
            .executeUpdate()
    }

    @Transactional
    fun findAll(): MutableList<PersonEntity> {
        return personRepositoryInterface.findAll()
    }

}

//@Repository
//@Transactional(Transactional.TxType.MANDATORY)
//interface PersonRepository: JpaRepository<PersonEntity, Long> {
//
//    @Query("SELECT person_id FROM persons WHERE lower(name) like lower(?1) and lower(birthday) like lower(?2)", nativeQuery = true)
//    fun getId(name: String, birthday: String): Long
//

//}