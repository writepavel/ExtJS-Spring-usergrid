package com.writepavel.usergrid

import spock.lang.Specification

/**
 * Spock Unit Test Specification
 *
 */
class UsergridControllerSpec extends Specification {


    void "should return proper greeting message!"() {
        setup:
        UsergridController ctlr = new UsergridController()

        when:
        String restString = ctlr.index()

        then:
        restString == 'Greetings from Spring Boot and CloudBees!'
    }
/*
    void "should save file in filesystem"(){
        setup:
        UsergridController ctlr = new UsergridController()

        when:
        String response = ctlr.users2("Users!")
        File localfile = new File(Helpers.USERS_FILE_NAME)

        then:
        localfile.exists()
    }
    */
}
