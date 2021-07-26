package com.example.demo.service

import com.example.demo.Convertor
import com.example.demo.model_xml.PersonsXml
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

import java.io.File

@Service
class MainService(
    private val personService: PersonService
) {

    @Bean
    fun start() {
        fromDbToXmlNew()
        fromXmlToDbNew()
    }

    private fun fromDbToXmlNew() {
        Convertor()
            .marshalling(
                PersonsXml(personService.getPersonsXml(personService.getPersons())),
                File("src/main/resources/PersonsNew.xml")
            )
    }

    private fun fromXmlToDbNew() {
        val personsXml: PersonsXml = Convertor().unmarshalling(File("src/main/resources/Persons.xml")) as PersonsXml
        personsXml.personsXml.forEach { person ->
            personService.addPerson(person)
        }
    }
}