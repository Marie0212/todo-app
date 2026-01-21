package de.todoapp.persistence;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

public class SqliteDatabase {

    private final String jdbcUrl;

    public SqliteDatabase(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(jdbcUrl);
        } catch (Exception e) {
            throw new IllegalStateException("Could not open SQLite connection: " + jdbcUrl, e);
        }
    }

    public void initSchema() {
        // erwartet schema.sql in src/main/resources/db/schema.sql
        try (var in = getClass().getClassLoader().getResourceAsStream("db/schema.sql")) {
            if (in == null) {
                throw new IllegalStateException("schema.sql not found in resources: db/schema.sql");
            }
            String sql;
            try (var br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                sql = br.lines().collect(Collectors.joining("\n"));
            }

            try (Connection c = getConnection(); Statement st = c.createStatement()) {
                st.executeUpdate(sql);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not init schema", e);
        }
    }
}
