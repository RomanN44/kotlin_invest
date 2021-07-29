package com.example.demo.service

import com.example.demo.model_xml.Persons
import org.springframework.stereotype.Service
import java.io.File
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller

@Service
class ConvertorService {
    fun marshalling(people: Persons, outputFile: File) {
        JAXBContext.newInstance(Persons::class.java)
            .createMarshaller().apply {
                setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
                marshal(people, outputFile)
            }
    }

    fun unmarshalling(path: String): Persons {
        val jaxbContext = JAXBContext.newInstance(Persons::class.java)
        val unmarshaller = jaxbContext.createUnmarshaller()
        return unmarshaller.unmarshal(File(path)) as Persons
    }
}