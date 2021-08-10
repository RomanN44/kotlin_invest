package com.example.demo.config

import com.example.demo.service.ConvectorService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.dsl.*
import org.springframework.integration.file.FileWritingMessageHandler
import org.springframework.integration.file.dsl.Files
import org.springframework.integration.file.support.FileExistsMode
import java.io.File

@Configuration
@EnableIntegration
class IntegrationConfig(
    val convectorService: ConvectorService
) {
    private val input = File("src/main/resources/input")
    private val archive = File("src/main/resources/archive")
    private val error = File("src/main/resources/error")

    @Bean
    fun xml(): DirectChannel = MessageChannels.direct().get()

    @Bean
    fun errors(): DirectChannel = MessageChannels.direct().get()

    @Bean
    fun filesFlow() = integrationFlow(
        Files.inboundAdapter(input),
        { poller { it.fixedDelay(500).maxMessagesPerPoll(1)}}
    ) {
        filter<File> { file ->
            (file.isFile && file.extension.toLowerCase() == "xml")
        }
        route<File> {
            when(convectorService.fromXmlToDb(it)) {
                true -> xml()
                false -> errors()
            }
        }
    }

    @Bean
    fun saveToArchive() = integrationFlow(xml()) {
        handle(replaceTo(archive))
    }

    @Bean
    fun deleteFiles() = integrationFlow(errors()) {
        handle(replaceTo(error))
    }

    private fun replaceTo(directory: File) = FileWritingMessageHandler(directory).apply {
        setFileExistsMode(FileExistsMode.REPLACE)
        setExpectReply(false)
        setDeleteSourceFiles(true)
    }

}