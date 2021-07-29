package com.example.demo.model_xml

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
data class Person(
    @XmlElement
    var name: String? = null,

    @XmlElement
    var birthday: String? = null,

    @XmlElement(name = "hobbies")
    var hobbies: Hobbies? = Hobbies(mutableListOf())
    ) {

    fun equalsWithoutHobbies(person: Person): Boolean {
        if(name == person.name && birthday == person.birthday)
            return true
        return false
    }

    fun isExistList(personList: List<Person>): Boolean {
        personList.forEach { person ->
            if(name == person.name && birthday == person.birthday)
                return true
        }
        return false
    }

    fun returnClone(personList: List<Person>): Person? {
        personList.forEach { person ->
            if(name == person.name && birthday == person.birthday)
                return person
        }
        return null
    }

}