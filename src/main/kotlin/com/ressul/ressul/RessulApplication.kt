package com.ressul.ressul

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
class RessulApplication
fun main(args: Array<String>) {
    runApplication<RessulApplication>(*args)
}
