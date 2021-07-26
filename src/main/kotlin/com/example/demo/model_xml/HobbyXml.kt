package com.example.demo.model_xml

import com.example.demo.entity.Hobby
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class HobbyXml(
    @XmlElement var complexity: Int? = null,
    @XmlElement var hobby_name: String? = null
    ) {

    fun equals(hobbyXml: HobbyXml): Boolean {
        if(complexity == hobbyXml.complexity && hobby_name.equals(hobbyXml.hobby_name))
            return true
        return false
    }

    fun isExistList(hobbysXml: MutableList<HobbyXml>): Boolean {
        hobbysXml.forEach { hobby ->
            if(complexity == hobby.complexity && hobby_name.equals(hobby.hobby_name))
                return true
        }
        return false
    }

    @JvmName("isExistList1")
    fun isExistList(hobbies: MutableList<Hobby>): Boolean {
        hobbies.forEach { hobby ->
            if(complexity == hobby.complexity && hobby_name.equals(hobby.hobby_name))
                return true
        }
        return false
    }
}