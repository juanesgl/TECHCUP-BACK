package edu.dosw.proyect.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import javax.sql.DataSource;

/**
 * Railway expone {@code DATABASE_URL} como {@code postgres://...}. Este bean evita depender
 * de {@code EnvironmentPostProcessor} (a veces no se registra igual en el fat JAR).
 */
@Configuration
@Conditional(RailwayDataSourceConfiguration.RailwayDatabaseUrlCondition.class)
public class RailwayDataSourceConfiguration {

    @Bean
    @org.springframework.context.annotation.Primary
    public DataSource railwayDataSource(Environment environment) {
        String databaseUrl = firstNonBlank(
                environment.getProperty("DATABASE_URL"),
                environment.getProperty("DATABASE_PRIVATE_URL"));

        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("org.postgresql.Driver");

        if (databaseUrl.startsWith("jdbc:")) {
            ds.setJdbcUrl(databaseUrl);
        } else {
            PostgresDatabaseUrlParser.Parsed parsed =
                    PostgresDatabaseUrlParser.parsePostgresDatabaseUrl(databaseUrl);
            ds.setJdbcUrl(parsed.jdbcUrl());
            ds.setUsername(parsed.username());
            ds.setPassword(parsed.password());
        }

        return ds;
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) {
            return a;
        }
        if (b != null && !b.isBlank()) {
            return b;
        }
        return "";
    }

    static final class RailwayDatabaseUrlCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Environment env = context.getEnvironment();
            String explicit = env.getProperty("SPRING_DATASOURCE_URL");
            String databaseUrl = firstNonBlank(
                    env.getProperty("DATABASE_URL"),
                    env.getProperty("DATABASE_PRIVATE_URL"));

            if (databaseUrl.isBlank()) {
                return false;
            }

            boolean useRailway = explicit == null || explicit.isBlank()
                    || PostgresDatabaseUrlParser.pointsToLocalDatabase(explicit);

            return useRailway;
        }

        private static String firstNonBlank(String a, String b) {
            if (a != null && !a.isBlank()) {
                return a;
            }
            if (b != null && !b.isBlank()) {
                return b;
            }
            return "";
        }
    }
}
