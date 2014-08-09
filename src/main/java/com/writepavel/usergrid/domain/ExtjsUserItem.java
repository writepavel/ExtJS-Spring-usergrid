package com.writepavel.usergrid.domain;

/**
 * Created by Pavel on 30.07.2014.
 */
public class ExtjsUserItem {

    /*
    email: "tom@andersen.com"
firstName: "Tomas"
groupName: "Root"
id: "MyApp.model.User-4"
lastName: "Andersen"
phoneNumber: "+79214344562"
     */
    private String groupName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String userId;
    private String groupId;
    private String id;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
