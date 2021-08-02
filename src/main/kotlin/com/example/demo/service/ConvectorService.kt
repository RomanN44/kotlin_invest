package com.example.demo.service

import com.example.demo.model_xml.Persons
import org.springframework.messaging.Message
import org.springframework.stereotype.Service
import java.io.File

@Service
class ConvectorService(
    val personService: PersonService,
    val jaxbService: JAXBService,
) {
    fun fromXmlToDb(file: File): Boolean =
        try{
            val persons: Persons = jaxbService.unmarshalling(file)
            persons.person.forEach { person ->
                personService.addPersonAndHisHobbies(person)
            }
            true
        } catch (e: Exception) {
            false
        }
}