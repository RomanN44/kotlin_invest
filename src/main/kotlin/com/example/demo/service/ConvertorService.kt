package com.example.demo.service

import com.example.demo.model_xml.PersonsXml
import org.springframework.stereotype.Service
import java.io.File
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller

@Service("convectorService")
class ConvertorService {
    fun marshalling(people: PersonsXml, outputFile: File) {
        JAXBContext.newInstance(PersonsXml::class.java)
            .createMarshaller().apply {
                setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
                marshal(people, outputFile)
            }
    }

    fun unmarshalling(inputFile: File): Any {
        val jaxbContext = JAXBContext.newInstance(PersonsXml::class.java)
        val unmarshaller = jaxbContext.createUnmarshaller()
        return unmarshaller.unmarshal(inputFile) as PersonsXml
    }
}