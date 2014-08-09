package com.writepavel.usergrid
import com.writepavel.usergrid.utility.UserTreeManager
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.io.ClassPathResource
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
                                //.run(Application.class)
                                .run(new ClassPathResource("XMLConfig-Annotation.xml"))
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
        ResponseEntity entity = new RestTemplate().getForEntity("http://localhost:8080/userstxt", String.class)
       // String response = ctlr.userTree("UserTree!")
        System.out.println ("Result of http://localhost:8080/userstxt is " + entity.body)
        UserTreeManager userTreeManager = context.getBean(UserTreeManager.class)
        File localfile = userTreeManager.getHumanReadableFile();

        then:
        localfile.exists()
    }
}
