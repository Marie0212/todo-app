package de.todoapp.persistence;

import de.todoapp.domain.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteCategoryRepository implements CategoryWriter, CategoryReader {

    private final String url;

    public SQLiteCategoryRepository(String url) {
        this.url = url;
        System.out.println(">>> SQLiteCategoryRepository AKTIV: " + url);
        init();
    }

    private void init() {
        String sql = """
            CREATE TABLE IF NOT EXISTS categories (
              id   INTEGER PRIMARY KEY,
              name TEXT NOT NULL UNIQUE
            );
            """;

        try (Connection c = DriverManager.getConnection(url);
             Statement st = c.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Category DB init failed", e);
        }
    }

    @Override
    public Category save(Category category) {

        String sql = """
            INSERT INTO categories(id, name)
            VALUES (?, ?)
            ON CONFLICT(id) DO UPDATE SET
              name = excluded.name
            """;

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, category.getId());
            ps.setString(2, category.getName());
            ps.executeUpdate();
            return category;

        } catch (SQLException e) {
            throw new RuntimeException("Save category failed", e);
        }
    }

    @Override
    public List<Category> findAll() {
        String sql = "SELECT id, name FROM categories ORDER BY id";
        List<Category> out = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                out.add(new Category(rs.getLong("id"), rs.getString("name")));
            }

            return out;

        } catch (SQLException e) {
            throw new RuntimeException("FindAll categories failed", e);
        }
    }
}
