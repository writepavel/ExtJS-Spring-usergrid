package com.writepavel.usergrid

import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Spock Integration Test Specification
 *
 * Adapted from Tomas Lin's sample:
 * https://github.com/tomaslin/gs-spring-boot-spock
 *
 */
class UsergridControllerIntegrationSpec extends Specification {

    @Shared
    @AutoCleanup
    ConfigurableApplicationContext context

    /**
     * Load the complete SpringApplication before running tests
     */
    void setupSpec() {
        Future future = Executors
                .newSingleThreadExecutor().submit(
                new Callable() {
                    @Override
                    public ConfigurableApplicationContext call() throws Exception {
                        return (ConfigurableApplicationContext) SpringApplication
                                .run(Application.class)
                    }
                })
        context = future.get(60, TimeUnit.SECONDS)
    }

    void "should return proper greeting message!"() {
        when:
        ResponseEntity entity = new RestTemplate().getForEntity("http://localhost:8080/hello-rest", String.class)

        then:
        entity.statusCode == HttpStatus.OK
        entity.body == 'Greetings from Spring Boot and CloudBees!'
    }

    void "should save file in filesystem"(){

        when:
        ResponseEntity entity = new RestTemplate().getForEntity("http://localhost:8080/users", String.class)
       // String response = ctlr.users("Users!")
        System.out.println ("Result of http://localhost:8080/users is " + entity.body)
        File localfile = new File(Helpers.USERS_FILE_NAME)

        then:
        localfile.exists()
    }
}
