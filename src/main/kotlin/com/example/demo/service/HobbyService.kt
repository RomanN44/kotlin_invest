package com.example.demo.service

import com.example.demo.entity.HobbyEntity
import com.example.demo.model_xml.Hobby
import com.example.demo.repository.HobbyRepository
import org.springframework.stereotype.Service

@Service
class HobbyService(
    private val hobbyRepository: HobbyRepository
) {
    fun getHobbiesByPerson(id: Long): MutableList<HobbyEntity>? {
        return id.let { hobbyRepository.findAllByPerson(it) }
    }

    fun convertToHobbiesXml(list: MutableList<HobbyEntity>) : MutableList<Hobby> {
        val hobbies: MutableList<Hobby> = mutableListOf()
        list.forEach { hobby ->
            hobbies.add(Hobby(hobby.complexity!!, hobby.hobby_name!!))
        }
        return hobbies
    }

    fun insertHobby(hobby: HobbyEntity) {
        hobbyRepository.save(HobbyEntity().apply {
            complexity = hobby.complexity
            hobby_name = hobby.hobby_name
            person = hobby.person
        })
    }

    fun deleteHobby(hobby: HobbyEntity) {
        hobbyRepository.delete(HobbyEntity().apply {
            complexity = hobby.complexity
            hobby_name = hobby.hobby_name
            person = hobby.person
        })
    }

    fun deleteHobby(person_id: Long) {
        hobbyRepository.delete(HobbyEntity().apply { person = person_id })
    }

}