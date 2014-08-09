package com.writepavel.usergrid.persistence.integrationTests;

/**
 * Created by Pavel on 03.08.2014.
 */

import com.writepavel.usergrid.config.JPAConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static com.writepavel.usergrid.domain.fixture.JPAAssertions.assertTableExists;
import static com.writepavel.usergrid.domain.fixture.JPAAssertions.assertTableHasColumn;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JPAConfiguration.class})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UserMappingIntegrationTests {

    @Autowired
    EntityManager manager;

    @Autowired
    DataSource dataSource;

    @Test
    public void thatItemCustomMappingWorks() throws Exception {

        assertTableExists(manager, "GROUPS");
        assertTableExists(manager, "USERS");

        assertTableHasColumn(manager, "GROUPS", "id");
        assertTableHasColumn(manager, "USERS", "firstName");
    }


    @Test
    public void baseHasRightNameAndLocation() throws Exception {
        String url = dataSource.getConnection().getMetaData().getURL();

        BeanDefinitionRegistry bdr = new SimpleBeanDefinitionRegistry();
        ClassPathBeanDefinitionScanner s = new ClassPathBeanDefinitionScanner(bdr);

        TypeFilter tf = new AssignableTypeFilter(DataSourceFactory.class);
        s.addIncludeFilter(tf);
        //s.scan();
        s.scan("org.springframework.jdbc.datasource.embedded",
                "org.springframework.data.jpa.repository.config",
                //"org.springframework.orm.hibernate4",
                "org.springframework.orm.jpa",
                "org.springframework.orm.jpa.vendor",
                "javax.persistence");
        bdr.getBeanDefinition("simpleDriverDataSourceFactory");
        String[] beans = bdr.getBeanDefinitionNames();
        System.out.println("\n\n\n-------------------------------" +
                "\n\n\nbaseHasRightNameAndLocation url = " + url + "\n\n\n" +
                "-------------------------------\n\n\n");
    }

}
