package com.example.demo.service

import com.example.demo.entity.HobbyEntity
import com.example.demo.entity.PersonEntity
import com.example.demo.model_xml.Hobbies
import com.example.demo.model_xml.Hobby
import com.example.demo.model_xml.Person
import com.example.demo.repository.PersonRepository
import org.springframework.stereotype.Service

@Service
class PersonService(
    private val personRepository: PersonRepository,
    private val hobbyService: HobbyService
) {
    fun getPersons(): MutableList<PersonEntity> = personRepository.findAll()

    fun addPersonAndHisHobbies(person: Person) {
        if(personRepository.findAll().any { it.name == person.name && it.birthday == person.birthday }) {
            if(person.hobbies != null) {
                val dbHobbiesXml = hobbyService.convertToHobbiesXml(hobbyService.getHobbiesByPerson(getId(person)!!)!!)
                val ignoreHobby = createIgnoreHobbiesList(person.hobbies!!.hobby, person)
                val deleteHobby = createDeleteHobbiesList(dbHobbiesXml, person)
                person.hobbies!!.hobby.forEach { hobby ->
                    if(!hobby.isExistList(ignoreHobby)) {
                        addHobbies(hobby, person)
                    }
                }
                deleteHobbies(deleteHobby, person)
            } else {
                hobbyService.deleteHobby(getId(person)!!)
            }
        } else {
            personRepository.save(PersonEntity().apply {
                name = person.name
                birthday = person.birthday
            })
            if(person.hobbies != null) {
                person.hobbies!!.hobby.forEach { hobby ->
                    addHobbies(hobby, person)
                }
            }
        }
    }

    private fun addHobbies(hobby: Hobby, person: Person) {
        hobbyService.insertHobby(
            HobbyEntity(
                null,
                hobby.complexity,
                hobby.hobby_name,
                getId(person)))
    }

    private fun deleteHobbies(hobbies: MutableList<Hobby>, person: Person) {
        hobbies.forEach { hobby ->
            hobbyService.deleteHobby(HobbyEntity(
                null,
                hobby.complexity,
                hobby.hobby_name,
                getId(person)))
        }
    }

    private fun createIgnoreHobbiesList(hobbies: MutableList<Hobby>, person: Person): MutableList<Hobby> {
        val ignoreHobby: MutableList<Hobby> = mutableListOf()
        hobbies.forEach { hobby ->
            if(hobby.isExistList(hobbyService.getHobbiesByPerson(getId(person)!!)!!)) {
                ignoreHobby.add(hobby)
            }
        }
        return ignoreHobby
    }

    private fun createDeleteHobbiesList(hobbies: MutableList<Hobby>, person: Person): MutableList<Hobby> {
        var flag = false
        val deleteHobby: MutableList<Hobby> = mutableListOf()
        hobbies.forEach { db_hobby ->
            person.hobbies?.hobby?.forEach { xml_hobby ->
                if(xml_hobby.equals(db_hobby))
                    flag = true
            }
            if(!flag)
                deleteHobby.add(db_hobby)
            flag = false
        }
        return deleteHobby
    }

    private fun getId(person: Person): Long? {
        return personRepository.findAllByNameLikeAndBirthdayLike(person.name!!, person.birthday!!)[0].id
    }
}