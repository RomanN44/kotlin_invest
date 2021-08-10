package com.example.demo.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ResponseBody

import org.springframework.web.bind.annotation.RequestMapping

import org.springframework.amqp.core.AmqpTemplate

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import java.util.logging.Logger


@Controller
class SampleController {
//    var logger = LoggerFactory.getLogger(SampleController::class.java)
//
//    @Autowired
//    var template: AmqpTemplate? = null
//    @RequestMapping("/emit")
//    @ResponseBody
//    fun queue1(): String {
//        logger.info("Emit to queue1")
//        template!!.convertAndSend("queue1", "Message to queue")
//        return "Emit to queue"
//    }
}