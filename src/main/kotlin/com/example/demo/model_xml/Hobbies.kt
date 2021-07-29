package com.example.demo.model_xml

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "hobbies")
data class Hobbies(
    @XmlElement(name = "hobby")
    var hobby: MutableList<Hobby> = mutableListOf()
)