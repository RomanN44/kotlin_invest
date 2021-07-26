package com.example.demo.service

import com.example.demo.entity.Person
import com.example.demo.model_xml.HobbiesXml
import com.example.demo.model_xml.HobbyXml
import com.example.demo.model_xml.PersonXml
import com.example.demo.repository.PersonRepository
import org.springframework.stereotype.Service

@Service
class PersonService(
    private val personRepository: PersonRepository,
    private val hobbyService: HobbyService
) {
    fun getPersons(): MutableList<Person> = personRepository.findAll()

    fun getPersonsXml(list: MutableList<Person>): MutableList<PersonXml> {
        val personsXml: MutableList<PersonXml> = mutableListOf()
        list.forEach { element ->
            personsXml.add(
                PersonXml(
                    element.name!!,
                    element.birthday!!,
                    HobbiesXml(
                        hobbyService.convertToHobbiesXml(
                            hobbyService.getHobbiesByPerson(element.id!!)!!
                        )
                    )
                )
            )
        }
        return personsXml
    }

    fun addPerson(personXml: PersonXml) {
        if(personRepository.findAll().any { it.name == personXml.name && it.birthday == personXml.birthday }) {
            if(personXml.hobbies != null) {

                val dbHobbiesXml = hobbyService.convertToHobbiesXml(
                    hobbyService.getHobbiesByPerson(getId(personXml.name!!, personXml.birthday!!)!!)!!)

                val ignoreHobbyXml: MutableList<HobbyXml> = mutableListOf()

                personXml.hobbies!!.hobbyXml.forEach { hobby ->
                    if(hobby.isExistList(hobbyService.getHobbiesByPerson(getId(personXml.name!!, personXml.birthday!!)!!)!!)) {
                        ignoreHobbyXml.add(hobby)
                    }
                }

                var flag = false
                val deleteHobbyXml: MutableList<HobbyXml> = mutableListOf()

                dbHobbiesXml.forEach { db_hobby ->
                    personXml.hobbies?.hobbyXml?.forEach { xml_hobby ->
                        if(xml_hobby.equals(db_hobby))
                            flag = true
                    }
                    if(!flag)
                        deleteHobbyXml.add(db_hobby)
                    flag = false
                }

                personXml.hobbies!!.hobbyXml.forEach { hobby ->
                    if(!hobby.isExistList(ignoreHobbyXml)) {
                        hobbyService.insertHobby(
                            hobby.complexity!!,
                            hobby.hobby_name!!,
                            getId(personXml.name!!, personXml.birthday!!)
                            !!)
                    }
                }

                deleteHobbyXml.forEach { hobby ->
                    hobbyService.deleteHobby(hobby.complexity!!, hobby.hobby_name!!, getId(personXml.name, personXml.birthday)!!)
                }
            } else {
                hobbyService.deleteHobby(getId(personXml.name, personXml.birthday)!!)
            }
        } else {
            personRepository.save(Person().apply {
                personXml.name; personXml.birthday
            })

            if(personXml.hobbies != null) {
                personXml.hobbies!!.hobbyXml.forEach { hobby ->
                    hobbyService.insertHobby(
                        hobby.complexity!!,
                        hobby.hobby_name!!,
                        getId(personXml.name!!, personXml.birthday!!)
                        !!)
                }
            }
        }
    }

    private fun getId(name: String?, birthday: String?): Long? {
        return personRepository.findAllByNameLikeAndBirthdayLike(name!!, birthday!!)[0].id
    }
}