package com.example.demo.Model

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "hobbies")
class Hobbies(@XmlElement(name = "hobby") var hobby: MutableCollection<Hobby>) { //КАКОГО ХРЕНА HOBBY ОБЪЯВЛЯЕТ ТЭГ
    constructor(): this(mutableListOf())
}