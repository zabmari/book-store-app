package com.mate.config;

import org.testcontainers.containers.MySQLContainer;

public class CustomMySqlContainer extends MySQLContainer<CustomMySqlContainer> {
    private static final String DB_IMAGE = "mysql:8.2.0";
    private static CustomMySqlContainer customMySqlContainer;

    private CustomMySqlContainer() {
        super(DB_IMAGE);
    }

    public static synchronized CustomMySqlContainer getInstance() {
        if (customMySqlContainer == null) {
            customMySqlContainer = new CustomMySqlContainer();
        }
        return customMySqlContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", customMySqlContainer.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", customMySqlContainer.getUsername());
        System.setProperty("TEST_DB_PASSWORD", customMySqlContainer.getPassword());
    }

    @Override
    public void stop() {
    }
}
