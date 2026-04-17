package edu.dosw.proyect.config;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

final class PostgresDatabaseUrlParser {

    private PostgresDatabaseUrlParser() {}

    record Parsed(String jdbcUrl, String username, String password) {}

    static Parsed parsePostgresDatabaseUrl(String url) {
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

    static boolean pointsToLocalDatabase(String jdbcOrRawUrl) {
        if (jdbcOrRawUrl == null || jdbcOrRawUrl.isBlank()) {
            return false;
        }
        String u = jdbcOrRawUrl.toLowerCase();
        return u.contains("localhost")
                || u.contains("127.0.0.1")
                || u.contains("[::1]");
    }
}
