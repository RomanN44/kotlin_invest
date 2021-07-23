package com.example.demo.Model

import com.example.demo.Entity.HobbyEntity
import com.sun.xml.internal.bind.AccessorFactory
import com.sun.xml.internal.bind.XmlAccessorFactory
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class Hobby(@XmlElement var complexity: Int, @XmlElement var hobby_name: String) {
    constructor(): this(-1, "No name")

    override fun toString(): String {
        return "Hobby(complexity=$complexity, hobby_name='$hobby_name')"
    }

    fun equals(hobby: Hobby): Boolean {
        if(complexity == hobby.complexity && hobby_name.equals(hobby.hobby_name))
            return true
        return false
    }

    fun isExistList(hobbies: MutableList<Hobby>): Boolean {
        hobbies.forEach {
            if(complexity == it.complexity && hobby_name.equals(it.hobby_name))
                return true
        }
        return false
    }

    @JvmName("isExistList1")
    fun isExistList(hobbies: MutableList<HobbyEntity>): Boolean? {
        hobbies.forEach {
            if(complexity == it.complexity && hobby_name.equals(it.hobby_name))
                return true
        }
        return false
    }

}