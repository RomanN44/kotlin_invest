package com.example.demo.config

import com.example.demo.receiver.DeviceMonitoringReceiver
import org.springframework.amqp.core.AcknowledgeMode
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.beans.factory.annotation.Qualifier

@Configuration
class RabbitConfiguration {
    private val defaultName = "newQueue"

    @Bean
    fun connectionFactoryBean() = RabbitConnectionFactoryBean().apply {
        this.setHost("localhost")
        this.setPort(5671)
        this.setUsername("admin")
        this.setPassword("admin")
        this.setVirtualHost("vhA")

        this.setUseSSL(true)
        this.setSslAlgorithm("TLSv1.2")

        this.setKeyStore("client_key.p12")
        this.setKeyStorePassphrase("rabbitstore")
        this.setTrustStore("server_store.jks")
        this.setTrustStorePassphrase("rabbitstore")
    }

    @Bean(name = ["GEO_RABBIT_CONNECTION"])
    fun connectionFactory(connectionFactoryBean: RabbitConnectionFactoryBean): ConnectionFactory =
        CachingConnectionFactory(connectionFactoryBean.`object`)

    @Bean
    fun myQueue() = Queue(defaultName, false, false, false, null)

    @Bean
    fun container(@Qualifier("GEO_RABBIT_CONNECTION") connectionFactory: ConnectionFactory, listenerAdapter: MessageListenerAdapter)=SimpleMessageListenerContainer().apply {
            this.connectionFactory = connectionFactory
            this.setQueueNames(defaultName)
            this.setMessageListener(listenerAdapter)
            this.acknowledgeMode = AcknowledgeMode.AUTO
        }

    @Bean
    fun listenerAdapter(deviceMonitoringReceiver: DeviceMonitoringReceiver?): MessageListenerAdapter? {
        return MessageListenerAdapter(deviceMonitoringReceiver, "receiveMessage")
    }
}