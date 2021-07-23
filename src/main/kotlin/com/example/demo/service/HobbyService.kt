package com.example.demo.service

import com.example.demo.Entity.HobbyEntity
import com.example.demo.Model.Hobby
import com.example.demo.Repository.HobbyRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class HobbyService(private val hobbyRepository: HobbyRepository) {
    @Transactional
    fun getHobbiesByPerson(id: Long): MutableList<HobbyEntity>? {
        return id?.let { hobbyRepository.getHobbiesByPerson(it) }
    }

    fun convertToHobbiesXml(list: MutableList<HobbyEntity>) : MutableList<Hobby> {
        val hobbies: MutableList<Hobby> = mutableListOf()
        list.forEach { hobby ->
            hobbies.add(Hobby(hobby.complexity, hobby.hobby_name))
        }
        return hobbies
    }

    @Transactional
    fun insertHobby(complexity: Int, hobby_name: String, person_id: Long) {
        hobbyRepository.insertHobby(complexity, hobby_name, person_id)
    }

    @Transactional
    fun deleteHobby(complexity: Int, hobby_name: String, person_id: Long) {
        hobbyRepository.deleteHobby(complexity, hobby_name, person_id)
    }

}