package com.writepavel.usergrid

import com.writepavel.usergrid.domain.UserTree
import com.writepavel.usergrid.domain.UserTreeBuilder
import com.writepavel.usergrid.utility.UserTreeManager
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

     private UserTreeBuilder emptyBuilder(){
        return new UserTreeBuilder();
    }
    private UserTreeBuilder builderWithUser(UserTreeBuilder builder){
        builder.addUser("Tomas", "Andersen", "tom@andersen.com", "+79214344562", null);
        return builder;
    }

    private UserTreeBuilder builderWithGroup(UserTreeBuilder builder){
        builder.addGroup("Footballers", null);
        return builder;
    }

    private UserTreeBuilder builderWithUserInGroup(UserTreeBuilder builder){
        builder.addToGroup("Footballers", null, "Iker", "Casillas", "iker@casillas.com", "+79112345463", null);
        return builder;
    }


    void "should add UserNodes to userTree" () {
        setup:
        //UserTreeBuilder builder = new UserTreeBuilder();

        expect:
        builder.userTree.toString() == userTreeString

        where:
        builder << [builderWithUser(emptyBuilder()),
                    builderWithUser(emptyBuilder()),
                    builderWithGroup(builderWithUser(emptyBuilder())),
                    builderWithUserInGroup(builderWithGroup(builderWithUser(emptyBuilder())))]
        /*builder << [bWithUser(),
                    bWithUser(),
                    bWithGroup(),
                    bWithUserInGroup()]*/
        userTreeString << [
        '''List of Users:

Tomas.Andersen. email: tom@andersen.com, phone number: +79214344562''',
        '''List of Users:

Tomas.Andersen. email: tom@andersen.com, phone number: +79214344562''',
        '''List of Users:

Tomas.Andersen. email: tom@andersen.com, phone number: +79214344562

Group of users: \"Footballers\"
''',
        '''List of Users:

Tomas.Andersen. email: tom@andersen.com, phone number: +79214344562

Group of users: \"Footballers\"

 - Iker.Casillas. email: iker@casillas.com, phone number: +79112345463''']

    }

    void "should parse correct userTree" () {

        setup:
        UserTree userTree1 = UserTreeManager.buildSampleUserTree();
        UserTreeManager.saveUsersToTxtFile(userTree1, ".", "testUserTree");
        UserTree userTree2 = UserTreeManager.readUserTreeFromTxtFile(new File("testUserTree.txt"))

        expect:
        userTree1.toString() == userTree2.toString()
    }
/*
    void "should save file in filesystem"(){
        setup:
        UsergridController ctlr = new UsergridController()

        when:
        String response = ctlr.users2("UserTree!")
        File localfile = new File(Helpers.USERS_FILE_NAME)

        then:
        localfile.exists()
    }
    */
}
