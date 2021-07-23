package com.example.demo


import com.example.demo.Dao.PersonDao
import com.example.demo.Model.Person
import com.example.demo.Model.Persons
import com.example.demo.service.HobbyService
import com.example.demo.service.PersonService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.File

@SpringBootApplication
class KotlinInvestApplication(
    var personDao: PersonDao,
    var personService: PersonService,
    var hobbyService: HobbyService
    ): CommandLineRunner {

    override fun run(vararg args: String?) {
        FromXmlToDbNew()
        FromDbToXmlNew()
    }

    fun FromDbToXml() {
        val convector = Convertor()
        val list = personDao.loadAllPersons()
        val persons = Persons(list)
        convector.marshalling(persons, File("src/main/resources/PersonsNew.xml"))
    }

    fun FromXmlToDb() {
        val convector = Convertor()
        val persons: Persons = convector.Unmarshalling(File("src/main/resources/Persons.xml")) as Persons
        personDao.addPersons(persons.person as MutableList<Person>)
    }

    fun FromDbToXmlNew() {
        Convertor()
            .marshalling(
                Persons(personService.getPersonsXml(personService.getPersons())),
                File("src/main/resources/PersonsNew.xml"))
    }

    fun FromXmlToDbNew() {
        val persons: Persons = Convertor().Unmarshalling(File("src/main/resources/Persons.xml")) as Persons
        personService.addPersons(persons.person as MutableList<Person>)
    }
}

fun main(args: Array<String>) {
    runApplication<KotlinInvestApplication>(*args)
}

