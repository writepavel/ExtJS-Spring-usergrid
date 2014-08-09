package com.writepavel.usergrid.domain;

import javax.persistence.*;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Pavel on 20.07.2014.
 */

@Entity(name = "GROUPS")
public class UserGroup implements UserNode {

    @Column(name = "text")
    private String text;

    @Column(name = "leaf")
    private boolean leaf = false;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;


    @OneToMany (cascade = CascadeType.MERGE/*, orphanRemoval = true, fetch = FetchType.EAGER*/)
    @JoinColumn(name="USER_ID"/*, insertable = false, updatable = false*/)
    //@NotFound(action= NotFoundAction.IGNORE)
    private Set<User> children = Collections.synchronizedSet(new LinkedHashSet<User>());

    public UserGroup(){
    }

    public UserGroup(String groupName, String groupId){
        this.text = groupName;
        this.id = (groupId == null) ? UUID.randomUUID().toString() : groupId;
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

    public Set<User> getChildren(){
        return children;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String groupId) {
        this.id = (groupId == null) ? UUID.randomUUID().toString() : groupId;
    }

    @Override
    public String toString(){
        return String.format("\nGroup of users: \"%1$s\"\n%2$s", text, UserTreeBuilder.childrenString(children, " - "));
    }


    @Override
    public boolean equals(Object o){
        if (!(o instanceof UserGroup)) return false;
        UserGroup group = (UserGroup) o;
        return ((text == group.text) &&
                (children == group.children));
    }

    @Override
    public int hashCode(){
        int hash = 1;
        hash = hash * 17 + text.hashCode();
        hash = hash * 31 + children.hashCode();
        return hash;
    }

}
