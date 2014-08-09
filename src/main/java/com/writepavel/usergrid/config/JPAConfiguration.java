package com.writepavel.usergrid.config;

/**
 * Created by Pavel on 03.08.2014.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(basePackages = "com.writepavel.usergrid.persistence.repository")
@EnableTransactionManagement
public class JPAConfiguration {

    @Bean
    public DataSource dataSource() throws SQLException {
        DerbyInFileEmbeddedDatabaseBuilder builder = new DerbyInFileEmbeddedDatabaseBuilder()
                .setName("usergriddb");
        return builder.build();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() throws SQLException {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setMappingResources();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.writepavel.usergrid.domain");
        factory.setDataSource(dataSource());
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    /**
     * Here we set different properties for our new DB
     * @param entityManagerFactory
     * @return
     */
    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        Map<String, String> properties = new HashMap<>();
        return entityManagerFactory.createEntityManager(properties);
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }
}
