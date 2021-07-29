package com.example.demo.model_xml

import com.example.demo.entity.HobbyEntity
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class Hobby(
    @XmlElement var complexity: Int? = null,
    @XmlElement var hobby_name: String? = null
    ) {

    fun equals(hobby: Hobby): Boolean {
        if(complexity == hobby.complexity && hobby_name.equals(hobby.hobby_name))
            return true
        return false
    }

    fun isExistList(hobbies: MutableList<Hobby>): Boolean {
        hobbies.forEach { hobby ->
            if(complexity == hobby.complexity && hobby_name.equals(hobby.hobby_name))
                return true
        }
        return false
    }

    @JvmName("isExistList1")
    fun isExistList(hobbyEntities: MutableList<HobbyEntity>): Boolean {
        hobbyEntities.forEach { hobby ->
            if(complexity == hobby.complexity && hobby_name.equals(hobby.hobby_name))
                return true
        }
        return false
    }
}