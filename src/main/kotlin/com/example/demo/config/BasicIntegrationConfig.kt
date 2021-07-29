package com.example.demo.config

import com.example.demo.main
import com.example.demo.service.MainService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.InboundChannelAdapter
import org.springframework.integration.annotation.Poller
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.core.GenericSelector
import org.springframework.integration.core.MessageSelector
import org.springframework.integration.core.MessageSource
import org.springframework.integration.dsl.*
import org.springframework.integration.file.FileReadingMessageSource
import org.springframework.integration.file.FileWritingMessageHandler
import org.springframework.integration.file.filters.SimplePatternFileListFilter
import org.springframework.integration.file.support.FileExistsMode
import org.springframework.integration.handler.BridgeHandler
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
import java.io.File
import java.io.FileFilter
import java.io.FilenameFilter
import java.util.concurrent.TimeUnit
import org.springframework.integration.dsl.MessageChannels

@Configuration
@EnableIntegration
class BasicIntegrationConfig(
    @Autowired
    val mainService: MainService
) {
    val inputDir: String = "src/main/resources/input"
    val outputDir: String = "src/main/resources/output"
    val errorDir: String = "src/main/resources/error"
    val archiveDir: String = "src/main/resources/archive"

    @Bean
    fun queueChannel(): MessageChannel? {
        return MessageChannels.queue().get()
    }

    @Bean
    fun publishSubscribe(): MessageChannel? {
        return MessageChannels.publishSubscribe().get()
    }

    @Bean
    @InboundChannelAdapter(value = "queueChannel", poller = [Poller(fixedDelay = "1000")])
    fun getInputDirectory(): MessageSource<File> {
        return FileReadingMessageSource().apply {
            this.setDirectory(File(inputDir))
            this.setAutoCreateDirectory(true)
            this.setFilter(SimplePatternFileListFilter("*.xml"))
        }
    }

    @ServiceActivator(inputChannel= "fileChannel")
    fun deleteFileHandler(): FileWritingMessageHandler {
        return FileWritingMessageHandler(File(outputDir)).apply {
            this.setFileExistsMode(FileExistsMode.REPLACE)
            this.setExpectReply(false)
        }
    }


    @Bean
    fun openInputFlow(): IntegrationFlow {
        return IntegrationFlows
            .from(getInputDirectory()){
                c -> c.poller(Pollers.fixedDelay(10000))
            }
            .fixedSubscriberChannel()
            .channel("queueChannel")
            .get()
    }

    @Bean
    fun onlyXml(): GenericSelector<File?>? {
        return GenericSelector { source -> source!!.name.endsWith(".xml") }
    }

    @Bean
    fun addPersonsFromXml(): IntegrationFlow {
        return IntegrationFlows
            .from(getInputDirectory()){
                    c -> c.poller(Pollers.fixedDelay(10000))
            }
            .handle(mainService, "fromXmlToDb")
            .get()
    }




}