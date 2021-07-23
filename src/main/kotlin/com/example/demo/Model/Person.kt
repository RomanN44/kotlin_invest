package com.example.demo.Model

import com.example.demo.Entity.PersonEntity
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
class Person(
    @XmlElement
    var name: String,

    @XmlElement
    var birthday: String,

    @XmlElement(name = "hobbies")
    var hobbies: Hobbies?
    ) {

    constructor(): this("No name", "No birthday", null)

    override fun toString(): String {
        return "Person(name='$name', birthday='$birthday')"
    }

    fun equalsWithoutHobbies(person: Person): Boolean {
        if(name.equals(person.name) && birthday.equals(person.birthday))
            return true
        return false
    }

    fun isExistList(personList: List<Person>): Boolean {
        personList.forEach {
            if(name == it.name && birthday == it.birthday)
                return true
        }
        return false
    }

    @JvmName("isExistList1")
    fun isExistList(personList: List<PersonEntity>): Boolean {
        personList.forEach { personEntity ->
            if(name.equals(personEntity.name) && birthday.equals(personEntity.birthday))
                return true
        }
        return false
    }

    fun returnClone(personList: List<Person>): Person? {
        personList.forEach {
            if(name.equals(it.name) && birthday.equals(it.birthday))
                return it
        }
        return null
    }

}