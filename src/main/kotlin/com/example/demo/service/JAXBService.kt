package com.example.demo.service

import com.example.demo.model_xml.Persons
import org.springframework.stereotype.Service
import java.io.File
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller

@Service
class JAXBService {
    fun unmarshalling(file: File): Persons =
        JAXBContext.newInstance(Persons::class.java)
            .createUnmarshaller()
            .unmarshal(file) as Persons
}