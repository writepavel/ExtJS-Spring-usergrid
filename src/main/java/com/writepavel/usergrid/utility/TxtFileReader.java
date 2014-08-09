package com.writepavel.usergrid.utility;

    import com.writepavel.usergrid.domain.UserTreeBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

    /**
     * Created by Pavel on 22.07.2014.
     * Reads human-readable file with user list and builds UserTree object.
     * Made using article http://www.martinfowler.com/articles/languageWorkbench.html
     */
    @Service
    public class TxtFileReader {
//(?<login>\\w+)
    //private static final String USER_PATTERN_STRING = "[^A-Za-z]*(?<firstName>[A-Za-z]*)\\.(?<lastName>[A-Za-z]*)\\.\\semail:\\s(?<email>[A-Za-z|@]*),\\sphone\\snumber:\\s(?<phoneNumber>[+|-|\\d]*).*";
    //private static final String USER_PATTERN_STRING = "[^A-Za-zА-Яа-я\\d]*(?<firstName>([^\\.\\s]||[A-Za-zА-Яа-я\\d])*)\\.(?<lastName>[A-Za-zА-Яа-я]*)\\.\\semail:\\s(?<email>[A-Za-zА-Яа-я|@|\\.]*),\\sphone\\snumber:\\s(?<phoneNumber>[+|-|\\d]*).*";
    //private static final String  USER_PATTERN_STRING = "[^A-Za-z|\\p{IsCyrillic}|\\d]*(?<firstName>[A-Za-z|\\p{IsCyrillic}|\\d]*)\\.(?<lastName>[A-Za-z|\\p{IsCyrillic}|\\d]*)\\.\\semail:\\s(?<email>[A-Za-z|\\p{IsCyrillic}|\\d|@|\\.]*),\\sphone\\snumber:\\s(?<phoneNumber>[+|-|\\d]*).*";
    private final String  USER_PATTERN_STRING = "[^A-Za-z|\\u0400-\\u04FF|\\d]*(?<firstName>[A-Za-z|\\u0400-\\u04FF|\\d]*)\\.(?<lastName>[A-Za-z|\\u0400-\\u04FF|\\d]*)\\.\\semail:\\s(?<email>[A-Za-z|\\d|@|\\.]*),\\sphone\\snumber:\\s(?<phoneNumber>[+-|\\d]*).*";
    private final String GROUP_PATTERN_STRING = "^Group of users: \"(?<groupName>[A-Za-z|\\u0400-\\u04FF|\\d|\\.]*)\"";

    private final Pattern  USER_PATTERN = Pattern.compile(USER_PATTERN_STRING);
    private final Pattern GROUP_PATTERN = Pattern.compile(GROUP_PATTERN_STRING);

    public void initBuilderByParsingFile(File file, UserTreeBuilder builder){
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\n");
            //scanner.useDelimiter(System.getProperty("line.separator"));
            while(scanner.hasNext()){
                processLine(scanner.next(), builder);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processLine(String line, UserTreeBuilder builder) {
        if (isBlank(line)) return;
        if (isComment(line)) return;
        if (isHelperLine(line)) return;
        if (isGroupDefinition(line)) {
            //System.out.println("FileReader. Group definition line: " + line);
            switchGroupFromLine(line, builder);
            return;
        }
        if (isUserDefinition(line)) {
            //System.out.println("FileReader. User definition line: " + line);
            addUserFromLine(line, builder);
            return;
        }
        else System.out.println("FileReader. Unidentified line: " + line);

    }



    private boolean isComment(String line) {
        return line.startsWith("#");
    }
    private boolean isBlank(String line) {
        return line.length() == 0;
    }

    // TODO Lines are not equal "something"
    // TODO
    private boolean isHelperLine(String line) {
        return line.matches("^List of Users:");
    }

    private boolean isGroupDefinition(String line) {
        return line.matches(GROUP_PATTERN_STRING);
    }

    private boolean isUserDefinition(String line) {
        //return line.matches(".*[A-Za-z]\\.[A-Za-z]\\. email: .*, phone number: .*");
        return line.matches(USER_PATTERN_STRING);
    }

    /**
     * Apply pattern to line and extracts value of pattern's group.
     * If group is not found, returns group name.
     * @return
     */
    private String getMatchGroupValue(Matcher m, String groupName){
        try {
           String groupValue = m.group(groupName);
            return groupValue;
        }
        catch (IllegalArgumentException e){
            return groupName;
        }
    }

    private void addUserFromLine(String line, UserTreeBuilder builder){
        Matcher m = USER_PATTERN.matcher(line);
        if (!m.find()) return;
        String firstName = getMatchGroupValue(m, "firstName");
        String lastName = getMatchGroupValue(m, "lastName");
        String email = getMatchGroupValue(m, "email");
        String phoneNumber = getMatchGroupValue(m, "phoneNumber");
        builder.addUser(firstName, lastName, email, phoneNumber, null); // text files does not contain IDs.
    }

    private void switchGroupFromLine(String line, UserTreeBuilder builder){
        Matcher m = GROUP_PATTERN.matcher(line);
        if (!m.find()) return;
        String groupName = getMatchGroupValue(m, "groupName");
        builder.setCurrentGroupName(groupName);
    }
}
