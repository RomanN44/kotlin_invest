package com.example.demo.service

import com.example.demo.model_xml.Persons
import org.springframework.messaging.Message
import org.springframework.stereotype.Service

import java.io.File

@Service
class MainService(
    private val personService: PersonService,
    private val convertorService: ConvertorService
) {

    fun fromDbToXml() {
        ConvertorService()
            .marshalling(
                Persons(personService.getPersonsXml(personService.getPersons())),
                File("src/main/resources/output/PersonsNew.xml")
            )
    }

    fun fromXmlToDb(message: Message<String>) {
        val persons: Persons = convertorService.unmarshalling(message.payload.replace('\\', '/'))
        persons.person.forEach { person ->
            personService.addPerson(person)
        }
    }
}