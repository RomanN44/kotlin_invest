package com.example.demo.controller

import com.example.demo.entity.HobbyEntity
import com.example.demo.entity.PersonEntity
import com.example.demo.model_xml.Person
import com.example.demo.service.HobbyService
import com.example.demo.service.PersonService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.RequestBody

@RestController
class MainController(
    val personService: PersonService,
    val hobbyService: HobbyService
) {
    @GetMapping("/persons")
    fun getPersons(): ResponseEntity<MutableList<PersonEntity>> {
        val persons = personService.getPersons()
        val headers = HttpHeaders()
        headers.add("token", "token value")
        return ResponseEntity.ok().headers(headers).body(persons);
    }

    @RequestMapping("/addPerson")
    fun addPerson(@RequestBody person: Person): ResponseEntity<Unit> {
        personService.addPersonAndHisHobbies(person)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/hobbiesByPerson")
    fun getHobbiesByPerson(@RequestBody id:Long): ResponseEntity<MutableList<HobbyEntity>> {
        val hobbies = hobbyService.getHobbiesByPerson(id)
        val headers = HttpHeaders()
        headers.add("token", "token value")
        return ResponseEntity.ok().headers(headers).body(hobbies);
    }

    @RequestMapping("/addHobby")
    fun addHobby(@RequestBody hobby: HobbyEntity): ResponseEntity<Unit> {
        hobbyService.insertHobby(hobby)
        return ResponseEntity.ok().build()
    }

    @RequestMapping("/deleteHobby")
    fun deleteHobby(@RequestBody hobby: HobbyEntity): ResponseEntity<Unit> {
        hobbyService.deleteHobby(hobby)
        return ResponseEntity.ok().build()
    }

}