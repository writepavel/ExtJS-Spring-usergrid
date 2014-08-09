package com.writepavel.usergrid.persistence.integrationTests;

/**
 * Created by Pavel on 03.08.2014.
 */

import com.writepavel.usergrid.config.JPAConfiguration;
import com.writepavel.usergrid.domain.User;
import com.writepavel.usergrid.domain.UserGroup;
import com.writepavel.usergrid.domain.UserTreeBuilder;
import com.writepavel.usergrid.persistence.repository.GroupsRepository;
import com.writepavel.usergrid.persistence.repository.UsersRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JPAConfiguration.class})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UsersRepositoryIntegrationTests {

    @Autowired
    UsersRepository usersRepo;

    @Autowired
    GroupsRepository groupsRepo;

    @Test
    public void thatItemIsInsertedIntoRepoWorks() throws Exception {
        String key = generateId();

        User user = new User("Tomas", "Andersen", "tom@andersen.com", "+79214344562", null);
        user.setFirstName("TOM");
        user.setId(key);

        Map<String, Integer> items = new HashMap<>();

        usersRepo.save(user);

        User retrievedUser = usersRepo.findOne(key);

        assertNotNull(retrievedUser);
        assertEquals(key, retrievedUser.getId());
        assertEquals("TOM", retrievedUser.getFirstName());
        //assertEquals(items.get("yummy1"), retrievedUser.getOrderItems().get("yummy1"));
    }

    private String generateId(){
        return UUID.randomUUID().toString();
    }

    @Test
    public void thatGroupContainsNewUsers() throws Exception {
        UserTreeBuilder b = new UserTreeBuilder();
        String groupKey = generateId();
        b.addToGroup("g1", groupKey, "Pavel", "Ponomarev", "pp@gmail.com", "899121222342", generateId());
        b.addToGroup("g1", groupKey, "Pavel2", "Ponomarev2", "p2p@gmail.com", "890121222342", generateId());
        HashMap groups = b.getGroupMap();
        System.out.println(groups);
        groupsRepo.save(groups.values());
        UserGroup g = groupsRepo.findOne(groupKey);

        assertNotNull(g);
        System.out.println(g);
        assertEquals(2, g.getChildren().size());
        assertEquals("Pavel", g.getChildren().iterator().next().getFirstName());
    }

}
