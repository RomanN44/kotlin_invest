package com.example.demo

import com.example.demo.service.MainService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinInvestApplication

fun main(args: Array<String>) {
    runApplication<KotlinInvestApplication>(*args)
}

