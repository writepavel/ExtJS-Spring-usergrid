package com.writepavel.usergrid.utility;

import com.writepavel.usergrid.domain.*;
import com.writepavel.usergrid.persistence.repository.GroupsRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Pavel on 13.07.2014.
 * This @Service component handles 6 main operations for UserTree: "reading" and "writing"
 * for three media: DB, human-readable file and Object, "reseived from"/"transmitted to" ExtJS client.
 * "Reading" operations are internally init request-scoped bean "userTreeBuilder"
 * "Writing" operations use contents of this bean to send users to different media.
 * <ul>
 * <li>init user Tree from human readable txt file by txtFileReader bean (internally it inits request scoped bean userTreeBuilder)</li>
 * <li>creates UserTree from ExtJS POST request, using userTreeBuilder</li>
 * <li>creates UserTree from DB, using userTreeBuilder</li>
 * <li>saves userTreeBuilder contents to human-readable file, using UserTree.toString()</li>
 * <li>saves userTreeBuilder contents to DB</li>
 * <li>sends UserTree from userTreeBuilder to UsergridController for transmitting to ExtJS client</li>
 * </ul>
 */
@Service
public class UserTreeManager implements ApplicationContextAware {

    public final String USERS_FILE_NAME = "userTree";

    @Autowired
    private TxtFileReader txtFileReader;

    private ApplicationContext applicationContext;

    /**
     * Application Context creates userTreeBuilder automatically on each request.
     * So for each request this object is "clean" and ready for gathering user information
     * from DB or from txt file by singleton userTreeManager.
     */
    @Autowired
    private UserTreeBuilder userTreeBuilder;

    @Autowired
    private GroupsRepository groupsRepository;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void saveUsersToYamlFile(UserTree userTree, String parent) {
        String yaml_data = (new Yaml()).dump(userTree);
        String yaml_data2 = object2yaml(userTree);
        File file = new File(parent, USERS_FILE_NAME + ".yaml");
        System.out.println(file.getPath());
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bufferFileWriter = new BufferedWriter(fw);
            //fw.write(yaml_data);
            new Yaml().dump(userTree, fw);
            bufferFileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void saveUsersToTxtFile() {
        saveUsersToTxtFile(userTreeBuilder.getUserTree(), USERS_FILE_NAME);
    }

    private void saveUsersToTxtFile(UserTree userTree, String filename) {
        try {
            File txtFile = getTxtFileFromContext(filename);

            //FileWriter fw = new FileWriter(file);
            //BufferedWriter bufferFileWriter = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(txtFile, "UTF-8");  // using PringWriter because of utf8 support.
            pw.write(userTree.toString());
            pw.flush();
            pw.close();
            //bufferFileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public UserTree readUserTreeFromTxtFile() {
        File file = getHumanReadableFile();
        System.out.println("Reading user Tree from file " + file.getAbsolutePath());
        txtFileReader.initBuilderByParsingFile(file, userTreeBuilder);
        return userTreeBuilder.getUserTree();
    }

    private String object2yaml(Object o) {
        Yaml yaml = new Yaml();
        return yaml.dump(o);
    }

    public UserTree buildSampleUserTree() {
        userTreeBuilder.addUser("Tomas", "Andersen", "tom@andersen.com", "+79214344562", null);
        userTreeBuilder.addGroup("Footballers", null);
        userTreeBuilder.addToGroup("Footballers", null, "Iker", "Casillas", "iker@casillas.com", "+79112345463", null);
        //  builder.addToGroup("Musicians", "Amadeus", "Mozart", "mozart@classics.com", "+79112345482");
        return userTreeBuilder.getUserTree();
    }

    public UserTree getUsersForExtjsClient() {
        return userTreeBuilder.getUserTree();
    }

    public void initUsersFromExtJSPostRequest(ArrayList<ExtjsUserItem> users) {
        for (ExtjsUserItem item : users) {
            userTreeBuilder.addToGroup(
                    item.getGroupName(),
                    item.getGroupId(),
                    item.getFirstName(),
                    item.getLastName(),
                    item.getEmail(),
                    item.getPhoneNumber(),
                    item.getUserId());
        }
    }

    public void saveUsersToDB() {
        groupsRepository.save(userTreeBuilder.getGroupMap().values());
    }



    public File getHumanReadableFile() {
        return getTxtFileFromContext(USERS_FILE_NAME);
    }

    public File getTxtFileFromContext(String fileName) {
        File webAppDir = getWebappDir();
        File txtFile;
        try {
            txtFile = new File(webAppDir, fileName + ".txt");
            if (!txtFile.exists()) txtFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return txtFile;
    }

    public File getWebappDir() {
        File webAppDir = null;
        try {
            webAppDir = applicationContext.getResource("/").getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return webAppDir;
    }


    /**
     * Returns users from db. If db is empty, returns null.
     */
    public void initUsersFromDB() {
        Iterable<UserGroup> dbGroups = groupsRepository.findAll();
        for (UserGroup group : dbGroups) {
            for (User user : group.getChildren()) {
                userTreeBuilder.addToGroup(
                        group.getText(),
                        group.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getId());
            }
        }
    }

    public void initUsersFromTxtFile() {
        txtFileReader.initBuilderByParsingFile(getHumanReadableFile(), userTreeBuilder);
    }
}
