package com.example.demo.model_xml

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "persons")
data class PersonsXml(
    @XmlElement
    var personsXml: MutableList<PersonXml> = mutableListOf()
)