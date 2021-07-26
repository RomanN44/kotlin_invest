package com.example.demo.service

import com.example.demo.entity.Hobby
import com.example.demo.model_xml.HobbyXml
import com.example.demo.repository.HobbyRepository
import org.springframework.stereotype.Service

@Service
class HobbyService(
    private val hobbyRepository: HobbyRepository
) {
    fun getHobbiesByPerson(id: Long): MutableList<Hobby>? {
        return id.let { hobbyRepository.findAllByPerson(it) }
    }

    fun convertToHobbiesXml(list: MutableList<Hobby>) : MutableList<HobbyXml> {
        val hobbiesXml: MutableList<HobbyXml> = mutableListOf()
        list.forEach { hobby ->
            hobbiesXml.add(HobbyXml(hobby.complexity!!, hobby.hobby_name!!))
        }
        return hobbiesXml
    }

    fun insertHobby(complexity: Int, hobby_name: String, person_id: Long) {
        hobbyRepository.save(Hobby().apply {
            complexity; hobby_name; person_id
        })
    }

    fun deleteHobby(complexity: Int, hobby_name: String, person_id: Long) {
        hobbyRepository.delete(Hobby().apply {
            complexity; hobby_name; person_id
        })
    }

    fun deleteHobby(person_id: Long) {
        hobbyRepository.delete(Hobby().apply { person_id })
    }

}