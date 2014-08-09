package com.writepavel.usergrid;

import com.writepavel.usergrid.domain.ExtjsUserItem;
import com.writepavel.usergrid.persistence.repository.GroupsRepository;
import com.writepavel.usergrid.utility.UserTreeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
public class UsergridController {

    @RequestMapping("/hello-rest")
    public String index() {
        return "Greetings from Spring Boot and CloudBees!";
    }

    @Autowired
    private UserTreeManager userTreeManager;

    @Autowired
    private GroupsRepository groupsRepository;

    @RequestMapping(value = "/userstxt",
                method = RequestMethod.GET
                    //,                headers = {"Content-type=application/json"}
                   )    
    public Object userstxt(HttpServletRequest request, HttpServletResponse response) {
        userTreeManager.initUsersFromTxtFile();
        userTreeManager.saveUsersToDB();
        //userTreeManager.saveUsersToTxtFile();
        return userTreeManager.getUsersForExtjsClient();
        /*if (userTree == null) {
            File webapp = userTreeManager.getWebappDir();
            File jsonfile = new File(webapp, "treegrid.json");
            System.out.println("UserTree = null, jsonfile = " + jsonfile);
            try {
                // get your file as InputStream
                FileInputStream inputStream = new FileInputStream(jsonfile.getAbsolutePath());
                // copy it to response's OutputStream
                IOUtils.copy(inputStream, response.getOutputStream());
                response.flushBuffer();
                //return response;
            } catch (IOException ex) {
                throw new RuntimeException("IOError writing file to output stream " + jsonfile.getAbsolutePath());
            }
        }        else {
            }*/
        }
    
    @RequestMapping(value = "/userstxt",
                method = RequestMethod.POST,
                headers = {"Content-type=application/json"})
@ResponseBody
public ResponseEntity<String> userstxt(@RequestBody ArrayList<ExtjsUserItem> users, HttpServletRequest request) {
    userTreeManager.initUsersFromExtJSPostRequest(users);
    userTreeManager.saveUsersToTxtFile();
        // if save into DB more than once from txt file, different instances
        // of the same entity will be created, because of different generated UUIDs.
        // userTreeManager.saveUsersToDB();
    //System.out.println("Updated user list from ExtJS client: " + userTree);
    return new ResponseEntity<>("", HttpStatus.OK);
}

    @RequestMapping(value = "/usersdb",
            method = RequestMethod.GET
    )
    public Object usersdb() {
        if (groupsRepository.count() == 0) {
            System.out.println("\n\n----------\n\nusersdb. Database has no users, so reading from txt file...\n\n----------\n\n");
            userTreeManager.initUsersFromTxtFile();
            // if save into DB from txt file more often than once, different instances
            // of the same entity will be created, because of different generated UUIDs.
            userTreeManager.saveUsersToDB();
        }
        else {
            userTreeManager.initUsersFromDB();
        }
        return userTreeManager.getUsersForExtjsClient();
    }

    @RequestMapping(value = "/usersdb",
            method = RequestMethod.POST,
            headers = {"Content-type=application/json"})
    @ResponseBody
    public ResponseEntity<String> usersdb(@RequestBody ArrayList<ExtjsUserItem> users, HttpServletRequest request) {
        userTreeManager.initUsersFromExtJSPostRequest(users);
        userTreeManager.saveUsersToDB();
        userTreeManager.saveUsersToTxtFile();
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
