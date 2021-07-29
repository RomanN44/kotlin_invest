package com.example.demo.model_xml

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "persons")
data class Persons(
    @XmlElement
    var person: MutableList<Person> = mutableListOf()
)