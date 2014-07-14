package com.writepavel.usergrid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class UsergridController {
/*
    @RequestMapping(value = "/users",
            method = RequestMethod.PUT,
            headers = {"Content-type=application/json"})
    @ResponseBody
    public JsonResponse addPerson(@RequestBody Person person) {
        //logger.debug(person.toString());
        return new JsonResponse("OK","");
    }
*/
    @RequestMapping("/hello-rest")
    public String index() {
        return "Greetings from Spring Boot and CloudBees!";
    }

   /* private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
*/

    Users users;

    @RequestMapping("/users")
    public Object users(HttpServletRequest request, HttpServletResponse response) {
		String webapp = getWebappPath (request);
        if (users == null) {
            File jsonfile = new File(webapp, "treegrid.json");
            System.out.println("Users = null, jsonfile = " + jsonfile);
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
        }
        else {
            Helpers.saveToFile(users, webapp);
            return users;}
        return null;
        }
		
	private String getWebappPath (HttpServletRequest request){
		HttpSession session = request.getSession();
        ServletContext sc = session.getServletContext();
        return sc.getRealPath("/");
	}

}
