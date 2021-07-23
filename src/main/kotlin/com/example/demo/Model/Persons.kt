package com.example.demo.Model

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "persons")
data class Persons(
    @XmlElement
    var person: MutableCollection<Person> = mutableListOf()
)