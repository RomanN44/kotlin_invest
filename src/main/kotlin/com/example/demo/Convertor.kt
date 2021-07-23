package com.example.demo

import com.example.demo.Model.Hobbies
import com.example.demo.Model.Persons
import java.io.File
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller

class Convertor {
    fun marshalling(people: Persons, outputFile: File) {
        JAXBContext.newInstance(Persons::class.java)
            .createMarshaller().apply {
                setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
                marshal(people, outputFile)
            }
    }

    fun Unmarshalling(inputFile: File): Any? {
        val jaxbContext = JAXBContext.newInstance(Persons::class.java)
        val unmarshaller = jaxbContext.createUnmarshaller()
        return unmarshaller.unmarshal(inputFile) as Persons
    }
}