package com.writepavel.usergrid;

import com.writepavel.usergrid.persistence.repository.GroupsRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;
import java.util.Map;


@Configuration
@EnableAutoConfiguration
// annotation @ComponentScan used for java-based server config
//@ComponentScan
public class Application {

    public static void main(String[] args) {
        // what to do with command-line args?
        /**
         * Creating context from java-based configuration
         */
        /*SpringApplication app = new SpringApplication(Application.class);
        app.setShowBanner(false);
        */
        /**
         * Creating context from annotation-based configuration
         */
        SpringApplication app = new SpringApplication(new ClassPathResource("XMLConfig-Annotation.xml"));

        ApplicationContext ctx = app.run();

        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }

        System.out.println("\n\n groupRepos: \n");

        Map<String, GroupsRepository> groupRepos = ctx.getBeansOfType(com.writepavel.usergrid.persistence.repository.GroupsRepository.class);
        for (Map.Entry<String, GroupsRepository> bean : groupRepos.entrySet()) {
            System.out.println(bean);
        }

    }

    @Bean
    public ServerProperties myServerProperties() {
        ServerProperties p = new ServerProperties();
        String portStr = System.getProperty("app.port");  // CloudBees Environment Variable for local port
        int port = (portStr != null) ? Integer.parseInt(portStr) : 8080;
        p.setPort(port);
        return p;
    }
}
