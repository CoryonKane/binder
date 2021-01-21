package com.codecool.binder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource postgresDataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");

        URI dbUri;
        try {
            dbUri = new URI(databaseUrl);
        } catch (URISyntaxException e) {
            System.out.println("Invalid DATABASE_URL: " + databaseUrl);
            return null;
        }

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath();

        // fully-qualified class name to distinguish from javax.sql.DataSource
        org.apache.tomcat.jdbc.pool.DataSource dataSource
                = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
