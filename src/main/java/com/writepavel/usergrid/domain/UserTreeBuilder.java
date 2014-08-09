package com.writepavel.usergrid.domain;

import com.writepavel.usergrid.persistence.repository.GroupsRepository;
import com.writepavel.usergrid.persistence.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Pavel on 20.07.2014.
 *
 * Application Context creates userTreeBuilder automatically on each request.
 * So for each request this object is "clean" and ready for gathering user information
 * from DB or from txt file by singleton UserTreeManager.
 */
@Service
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserTreeBuilder {

    @Autowired
    private UsersRepository usersRep;

    @Autowired
    private GroupsRepository groupsRepository;

    private HashMap<String, UserGroup> groupMap = new HashMap<>();

    private static final String ROOT_GROUP_NAME = "ROOT";
    private static final String ROOT_GROUP_ID = "root";
    private final UserGroup rootGroup = new UserGroup(ROOT_GROUP_NAME, ROOT_GROUP_ID);
    {
        getGroupMap().put(ROOT_GROUP_NAME, rootGroup);
    }

    private String currentGroupName = ROOT_GROUP_NAME;
    private String currentGroupId   = null;



    /**
     * Returns human-readable representation of contents of UserTree.
     * @param userNodes UserTree elements
     * @return human-readable string, representation of UserTree's contents
     */
    static String childrenString(Set<UserNode> userNodes){
        return childrenString(userNodes, "");
    }

    /**
     * Returns human-readable representation of contents of UserTree.
     * @param userNodes - UserTree elements
     * @param prefix - offset string for every element.
     * @return human-readable string, representation of UserTree's contents
     */
    static String childrenString(Set<? extends UserNode> userNodes, String prefix){
        StringBuffer buffer = new StringBuffer();
        for(UserNode child : userNodes){
            if (child != null)
            buffer.append('\n' + prefix + child.toString());
        }
        return buffer.toString();
    }

    /**
     * Inserts new User of new UserGroup into builder.
     * New Users insert into default rootGroup.
     * @param node
     */
    public void addChild(UserNode node){
        if (node instanceof User){
            addUser((User) node);
        }
        else if (node instanceof UserGroup){
            addGroup(node.getText(), node.getId());
        }
    }

    /**
     * Inserts new User into builder.
     * @param firstName
     * @param lastName
     * @param email
     * @param phoneNumber
     */
    public void addUser(String firstName, String lastName, String email, String phoneNumber, String userId){
        addToGroup(currentGroupName, currentGroupId, firstName, lastName, email, phoneNumber, userId);
    }

    /**
     * Default insert of new user into userTree. By default in inserts into ROOT group.
     * @param user
     */
    public void addUser(User user){
        addUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getId());
    }

    /**
     * Inserts new group into builder, if this group does is not exist.
     * If group exists, returns existing group by given groupName.
     * @param groupName
     */
    public void addGroup(String groupName, String groupId){
        createGroupIfNotExist(groupName, groupId);
    }

    /**
     * If group exists in groupMap, it extracts from Map.
     * Else returns new group and put it into groupMap.
     * If groupName.toUpperCase() is ROOT, returns root group.
     * @param groupName
     * @param groupId
     * @return
     */
    private UserGroup createGroupIfNotExist(String groupName, String groupId){
        if (groupName.toUpperCase().equals(ROOT_GROUP_NAME)) {
            return rootGroup;
        }
        if (!getGroupMap().containsKey(groupName)) {
            UserGroup group = new UserGroup(groupName, groupId);
            getGroupMap().put(groupName, group);
            return group;
        }
        else return getGroupMap().get(groupName);
    }

    /**
     * Inserts new User into group.
     * @param groupName
     * @param firstName
     * @param lastName
     * @param email
     * @param phoneNumber
     */
    public void addToGroup(String groupName, String groupId, String firstName, String lastName, String email, String phoneNumber, String userId){
//        System.out.println("\n\nUserTreeBuilder.addToGroup. This is "+ this +"\n\n");
//        System.out.println("groupMap = " + getGroupMap());
        //System.out.println("groupName = [" + groupName + "], groupId = [" + groupId + "], firstName = [" + firstName + "], lastName = [" + lastName + "], email = [" + email + "], phoneNumber = [" + phoneNumber + "], userId = [" + userId + "]");
        UserGroup group = createGroupIfNotExist(groupName, groupId);
        group.getChildren().add(new User(firstName, lastName, email, phoneNumber, userId));
        getGroupMap().put(groupName, group);

    }



    /**
     * @return UserTree. Converts internal hashmap "group name -> user" to ArrayList.
     * So it can be correctly handled by JSON-generator and has human-readable "toString".
     */
    public UserTree getUserTree() {
        UserTree userTree = new UserTree();
        for (User user : rootGroup.getChildren()){
            userTree.add(user);
        }
        for (Map.Entry<String, UserGroup> entry : getGroupMap().entrySet()){
            if (entry.getKey().toUpperCase().equals(ROOT_GROUP_NAME)) continue;
            userTree.add(entry.getValue());
        }
        return userTree;
    }

    /**
     * New Users are adding to "current group".
     */
    public String getCurrentGroupName() {
        return currentGroupName;
    }

    public void setCurrentGroupName(String currentGroupName) {
        this.currentGroupName = currentGroupName;
    }

    public GroupsRepository getGroupsRepository() {
        System.out.println("getGroupsRepository. REPO = " + groupsRepository);
        if (groupsRepository == null) {
            System.out.println("groupsRepository == null! It is not initialized!");
        }
        return groupsRepository;
    }

    /**
     * Class builds and manages UserTree object. It is used by DslParser.
     * Internally, before they are converted to ArrayList for JSON,
     * all users stored in HashMap "groupName -> UserGroup"
     * Root group named "ROOT"
     * Result: faster inserting and searching users to groups.
     */
    public HashMap<String, UserGroup> getGroupMap() {
        return groupMap;
    }
}
