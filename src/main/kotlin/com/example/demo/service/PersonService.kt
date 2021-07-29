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

    fun getPersonsXml(list: MutableList<PersonEntity>): MutableList<Person> {
        val persons: MutableList<Person> = mutableListOf()
        list.forEach { element ->
            persons.add(
                Person(
                    element.name!!,
                    element.birthday!!,
                    Hobbies(
                        hobbyService.convertToHobbiesXml(
                            hobbyService.getHobbiesByPerson(element.id!!)!!
                        )
                    )
                )
            )
        }
        return persons
    }

    fun addPerson(person: Person) {
        if(personRepository.findAll().any { it.name == person.name && it.birthday == person.birthday }) {
            if(person.hobbies != null) {
                val dbHobbiesXml = hobbyService.convertToHobbiesXml(
                    hobbyService.getHobbiesByPerson(getId(person.name!!, person.birthday!!)!!)!!)
                val ignoreHobby: MutableList<Hobby> = mutableListOf()
                person.hobbies!!.hobby.forEach { hobby ->
                    if(hobby.isExistList(hobbyService.getHobbiesByPerson(getId(person.name!!, person.birthday!!)!!)!!)) {
                        ignoreHobby.add(hobby)
                    }
                }
                var flag = false
                val deleteHobby: MutableList<Hobby> = mutableListOf()
                dbHobbiesXml.forEach { db_hobby ->
                    person.hobbies?.hobby?.forEach { xml_hobby ->
                        if(xml_hobby.equals(db_hobby))
                            flag = true
                    }
                    if(!flag)
                        deleteHobby.add(db_hobby)
                    flag = false
                }
                person.hobbies!!.hobby.forEach { hobby ->
                    if(!hobby.isExistList(ignoreHobby)) {
                        hobbyService.insertHobby(
                            HobbyEntity(
                                null,
                                hobby.complexity,
                                hobby.hobby_name,
                                getId(person.name!!, person.birthday!!)))
                    }
                }
                deleteHobby.forEach { hobby ->
                    hobbyService.deleteHobby(HobbyEntity(
                        null,
                        hobby.complexity,
                        hobby.hobby_name,
                        getId(person.name!!, person.birthday!!)))
                }
            } else {
                hobbyService.deleteHobby(getId(person.name, person.birthday)!!)
            }
        } else {
            personRepository.save(PersonEntity().apply {
                name = person.name
                birthday = person.birthday
            })
            if(person.hobbies != null) {
                person.hobbies!!.hobby.forEach { hobby ->
                    hobbyService.insertHobby(
                        HobbyEntity(
                            null,
                            hobby.complexity,
                            hobby.hobby_name,
                            getId(person.name!!, person.birthday!!)))
                }
            }
        }
    }

    private fun getId(name: String?, birthday: String?): Long? {
        return personRepository.findAllByNameLikeAndBirthdayLike(name!!, birthday!!)[0].id
    }
}