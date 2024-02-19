package com.ressul.ressul

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class RessulApplication

fun main(args: Array<String>) {
    runApplication<RessulApplication>(*args)
}
