package com.krgcorporate.postmanrunner;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

public class MongoDbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Container
    static final MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:4.4.4");

    static {
        mongoDbContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

        TestPropertyValues values = TestPropertyValues.of(
                "spring.data.mongodb.host=" + mongoDbContainer.getContainerIpAddress(),
                "spring.data.mongodb.port=" + mongoDbContainer.getFirstMappedPort()

        );
        values.applyTo(configurableApplicationContext);
    }
}
