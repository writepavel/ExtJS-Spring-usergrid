package com.writepavel.usergrid;

import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.IOException;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.*;

/**
 * Created by Pavel on 13.07.2014.
 */
public class Helpers {

    public static final String USERS_FILE_NAME = "users-file.yaml";

    static void saveToFile(Users users, String parent) {
        String yaml_data = (new Yaml()).dump(users);
        String yaml_data2 = object2yaml(users);
        File file = new File(parent, USERS_FILE_NAME);
        System.out.println(file.getPath());
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bufferFileWriter = new BufferedWriter(fw);
            //fw.write(yaml_data);
            new Yaml().dump(users, fw);
            bufferFileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String object2yaml (Object o) {
        Yaml yaml = new Yaml();
        return yaml.dump(o);
    }

	
	/** 
	* Now this is incorrect method from sample. It has to be realized.
	  TODO Realize correct Users class and convert JSON to Users
	*/
    static Users jsonFile2Users (String jsonFilePath) {
        JsonFactory jfactory = new JsonFactory();

        /*** read from file ***/
        try {
            JsonParser jParser = jfactory.createJsonParser(new File(jsonFilePath));

            // loop until token equal to "}"
            while (jParser.nextToken() != JsonToken.END_OBJECT) {

                String fieldname = jParser.getCurrentName();
                if ("name".equals(fieldname)) {

                    // current token is "name",
                    // move to next, which is "name"'s value
                    jParser.nextToken();
                    System.out.println(jParser.getText()); // display mkyong

                }

                if ("age".equals(fieldname)) {

                    // current token is "age",
                    // move to next, which is "name"'s value
                    jParser.nextToken();
                    System.out.println(jParser.getIntValue()); // display 29

                }

                if ("messages".equals(fieldname)) {

                    jParser.nextToken(); // current token is "[", move next

                    // messages is array, loop until token equal to "]"
                    while (jParser.nextToken() != JsonToken.END_ARRAY) {

                        // display msg1, msg2, msg3
                        System.out.println(jParser.getText());

                    }

                }

            }
            jParser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
