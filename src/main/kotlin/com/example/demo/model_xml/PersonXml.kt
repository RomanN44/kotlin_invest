package com.example.demo.model_xml

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
data class PersonXml(
    @XmlElement
    var name: String? = null,

    @XmlElement
    var birthday: String? = null,

    @XmlElement(name = "hobbies")
    var hobbies: HobbiesXml? = HobbiesXml(mutableListOf())
    ) {

    fun equalsWithoutHobbies(personXml: PersonXml): Boolean {
        if(name == personXml.name && birthday == personXml.birthday)
            return true
        return false
    }

    fun isExistList(personXmlList: List<PersonXml>): Boolean {
        personXmlList.forEach { person ->
            if(name == person.name && birthday == person.birthday)
                return true
        }
        return false
    }

    fun returnClone(personXmlList: List<PersonXml>): PersonXml? {
        personXmlList.forEach { person ->
            if(name == person.name && birthday == person.birthday)
                return person
        }
        return null
    }

}