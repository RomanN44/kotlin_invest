package com.example.demo.model_xml

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "hobbies")
data class HobbiesXml(
    @XmlElement(name = "hobby")
    var hobbyXml: MutableList<HobbyXml> = mutableListOf()
)