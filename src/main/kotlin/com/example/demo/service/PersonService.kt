package com.example.demo.service

import com.example.demo.Entity.PersonEntity
import com.example.demo.Model.Hobbies
import com.example.demo.Model.Hobby
import com.example.demo.Model.Person
import com.example.demo.Repository.PersonRepository
import org.jetbrains.annotations.NotNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class PersonService(
    private var personRepository: PersonRepository,
    private var hobbyService: HobbyService
) {
    fun getPersons(): MutableList<PersonEntity> = personRepository.findAll()


    fun getPersonsXml(list: MutableList<PersonEntity>): MutableList<Person> {
        val persons: MutableList<Person> = mutableListOf()
        list.forEach { element ->
            persons.add(
                Person(
                    element.name,
                    element.birthday,
                    hobbyService.getHobbiesByPerson(element.id)?.let {
                        hobbyService.convertToHobbiesXml(
                            it
                        )
                    }?.let {
                        Hobbies(
                            it
                        )
                    }))
        }
        return persons
    }

    fun addPersons(list: MutableList<Person>) {
        val ignoreList: MutableList<PersonEntity> = mutableListOf()
        personRepository.findAll().filter { it.name ==  }.forEach {
                ignoreList.add(it)
        }
        list.forEach { person ->
            if(!person.isExistList(ignoreList)) {
                personRepository.insertPerson(person.name, person.birthday)
                if (person.hobbies != null)
                   person.hobbies!!.hobby.forEach { hobby ->
                       getId(person.name, person.birthday).let {
                           hobbyService.insertHobby(hobby.complexity, hobby.hobby_name,
                               it
                           )
                       }
                   }
            } else {
                if (person.hobbies != null) {
                    val dbHobby: MutableList<Hobby> =
                        hobbyService.getHobbiesByPerson(getId(person.name, person.birthday))?.let {
                            hobbyService
                                .convertToHobbiesXml(
                                    it
                                )
                        }!!
                    val ignoreHobby: MutableList<Hobby> = mutableListOf()
                    val deleteHobby: MutableList<Hobby> = mutableListOf()
                    person.hobbies!!.hobby.forEach { hobby ->
                        if(hobby.isExistList(
                                hobbyService.getHobbiesByPerson(
                                    getId(person.name, person.birthday))!!
                            ) == true) {
                            ignoreHobby.add(hobby)
                        }
                    }
                    var flag = false
                    dbHobby.forEach { db_hobby ->
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
                            getId(person.name, person.birthday).let {
                                hobbyService.insertHobby(hobby.complexity, hobby.hobby_name,
                                    it
                                )
                            }
                        }
                    }
                    deleteHobby.forEach { hobby ->
                        getId(person.name, person.birthday).let {
                            hobbyService.deleteHobby(hobby.complexity, hobby.hobby_name,
                                it
                            )
                        }
                    }
                }
            }
        }
    }

    @Transactional
    @NotNull
    fun getId(name: String, birthday: String): Long {
        return personRepository.getId(name, birthday)
    }

    @Transactional
    fun addPerson(person: Person) {
        personRepository.insertPerson(person.name, person.birthday)
    }
}