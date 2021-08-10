package com.example.demo.client

import com.example.demo.config.RabbitConfiguration
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.net.SocketException

@Component
class RabbitClient(
    @Qualifier("GEO_RABBIT_CONNECTION")
    private val factory: ConnectionFactory
) {
    @Scheduled(fixedRate = 5000)
    fun sendMessageToChannel() {
        //        //TODO поле должно передаваться из файла RabbitConfiguration.kt
        val channelName = "newQueue"
        val message = "Message :)"
        val connection = factory.createConnection()
        val channel = connection.createChannel(false)
        channel.queueDeclare(channelName, false, false, false, null)
        channel.basicPublish("", channelName, null, message.toByteArray(Charsets.UTF_8))
        channel.close()
        connection.close()
    }
}