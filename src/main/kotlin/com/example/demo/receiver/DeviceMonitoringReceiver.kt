package com.example.demo.receiver

import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.stereotype.Component

@Component
class DeviceMonitoringReceiver: MessageListener {
    override fun onMessage(message: Message?) {
        if (message != null) {
            println("Gained ${String(message.body)}")
        } else {
            println("Gained empty message")
        }
    }
}