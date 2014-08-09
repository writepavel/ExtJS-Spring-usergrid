package com.writepavel.usergrid.domain;

/**
 * Created by Pavel on 20.07.2014.
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name = "USERS")
public class User implements UserNode, Comparable<User> {

    @Column(name = "text")
    private String text;

    @Column(name = "leaf")
    private boolean leaf = true;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Id
    //@Column(name = "USER_ID", unique = true, nullable = false)
    private String id;

    public User() {}

    public User (String firstName, String lastName, String email, String phoneNumber, String id){
        this.id = (id == null) ? UUID.randomUUID().toString() : id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.text = firstName + "." + lastName;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    @Override
    public boolean isLeaf() {
        return leaf;
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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = (id == null) ? UUID.randomUUID().toString() : id;
    }

    @Override
    public String toString(){
        return String.format("%1$s. email: %2$s, phone number: %3$s", text, email, phoneNumber);
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return ((text.toUpperCase() == user.text.toUpperCase()) &&
                (email.toUpperCase() == user.email.toUpperCase()) &&
                (phoneNumber.toUpperCase() == user.phoneNumber.toUpperCase()));
    }

    @Override
    public int hashCode(){
        int hash = 1;
        hash = hash * 17 + text.toUpperCase().hashCode();
        hash = hash * 31 + email.toUpperCase().hashCode();
        hash = hash * 13 + phoneNumber.toUpperCase().hashCode();
        hash = hash * 31 + id.toUpperCase().hashCode();
        return hash;
    }

    @Override
    public int compareTo(User user) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if (this == user) return EQUAL;


        int comparison = this.id.toUpperCase().compareTo(user.id.toUpperCase());
        if (comparison != EQUAL) return comparison;

        comparison = this.text.toUpperCase().compareTo(user.text.toUpperCase());
        if (comparison != EQUAL) return comparison;

        comparison = this.email.toUpperCase().compareTo(user.email.toUpperCase());
        if (comparison != EQUAL) return comparison;

        comparison = this.phoneNumber.toUpperCase().compareTo(user.phoneNumber.toUpperCase());
        if (comparison != EQUAL) return comparison;

        return EQUAL;
    }
}
