package de.todoapp.persistence;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SqliteDatabase {
    private final String jdbcUrl;

    public SqliteDatabase(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public Connection openConnection() throws Exception {
        return DriverManager.getConnection(jdbcUrl);
    }

    public void initSchema() {
        try (InputStream in = getClass().getResourceAsStream("/db/schema.sql")) {
            if (in == null) {
                throw new IllegalStateException("Resource not found: /db/schema.sql");
            }
            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            try (Connection con = openConnection();
                 Statement st = con.createStatement()) {
                st.execute(sql);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
