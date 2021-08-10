package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@SpringBootApplication
@EnableScheduling
class KotlinInvestApplication

fun main(args: Array<String>) {
    runApplication<KotlinInvestApplication>(*args)
}


