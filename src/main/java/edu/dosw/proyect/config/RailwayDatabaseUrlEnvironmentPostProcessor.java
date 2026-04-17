package edu.dosw.proyect.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Railway (y Heroku) exponen {@code DATABASE_URL} como {@code postgres://...}.
 * Spring espera {@code jdbc:postgresql://...} y credenciales por separado.
 */
public class RailwayDatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String explicit = environment.getProperty("SPRING_DATASOURCE_URL");
        String databaseUrl = firstNonBlank(
                environment.getProperty("DATABASE_URL"),
                environment.getProperty("DATABASE_PRIVATE_URL"));

        boolean useRailwayUrl = databaseUrl != null && !databaseUrl.isBlank()
                && (explicit == null || explicit.isBlank() || pointsToLocalDatabase(explicit));

        if (!useRailwayUrl) {
            if (explicit != null && !explicit.isBlank()) {
                return;
            }
            return;
        }

        Map<String, Object> map = new HashMap<>();
        if (databaseUrl.startsWith("jdbc:")) {
            map.put("spring.datasource.url", databaseUrl);
        } else if (databaseUrl.startsWith("postgres")) {
            Parsed parsed = parsePostgresDatabaseUrl(databaseUrl);
            map.put("spring.datasource.url", parsed.jdbcUrl());
            map.put("spring.datasource.username", parsed.username());
            map.put("spring.datasource.password", parsed.password());
        } else {
            return;
        }

        environment.getPropertySources().addFirst(new MapPropertySource("railwayDatabaseUrl", map));
    }

    /**
     * Si en Railway quedó pegada una URL de desarrollo (localhost), debemos ignorarla
     * cuando exista {@code DATABASE_URL} del plugin Postgres.
     */
    private static boolean pointsToLocalDatabase(String jdbcOrRawUrl) {
        String u = jdbcOrRawUrl.toLowerCase();
        return u.contains("localhost")
                || u.contains("127.0.0.1")
                || u.contains("[::1]");
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) {
            return a;
        }
        if (b != null && !b.isBlank()) {
            return b;
        }
        return null;
    }

    private record Parsed(String jdbcUrl, String username, String password) {}

    private static Parsed parsePostgresDatabaseUrl(String url) {
        String normalized = url.replaceFirst("^postgres(ql)?://", "");
        int at = normalized.lastIndexOf('@');
        if (at < 0) {
            throw new IllegalArgumentException("DATABASE_URL inválida: falta '@' entre credenciales y host");
        }
        String userInfo = normalized.substring(0, at);
        String rest = normalized.substring(at + 1);

        String user;
        String password;
        int colon = userInfo.indexOf(':');
        if (colon < 0) {
            user = URLDecoder.decode(userInfo, StandardCharsets.UTF_8);
            password = "";
        } else {
            user = URLDecoder.decode(userInfo.substring(0, colon), StandardCharsets.UTF_8);
            password = URLDecoder.decode(userInfo.substring(colon + 1), StandardCharsets.UTF_8);
        }

        int slash = rest.indexOf('/');
        if (slash < 0) {
            throw new IllegalArgumentException("DATABASE_URL inválida: falta nombre de base en la ruta");
        }
        String hostPort = rest.substring(0, slash);
        String pathAndQuery = rest.substring(slash);

        String jdbcUrl = "jdbc:postgresql://" + hostPort + pathAndQuery;
        return new Parsed(jdbcUrl, user, password);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
