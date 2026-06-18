package de.todoapp.persistence;

import de.todoapp.domain.Category;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class SQLiteCategoryRepository implements CategoryWriter, CategoryReader {

    private static final Table<?> CATEGORIES = DSL.table(DSL.name("categories"));

    private static final Field<Long> ID = DSL.field(DSL.name("id"), Long.class);
    private static final Field<String> NAME = DSL.field(DSL.name("name"), String.class);

    private final String url;

    public SQLiteCategoryRepository(String url) {
        this.url = url;
        System.out.println(">>> SQLiteCategoryRepository AKTIV: " + url);
        init();
    }

    private void init() {
        withContext(ctx -> {
            ctx.createTableIfNotExists(CATEGORIES)
                    .column(ID, SQLDataType.BIGINT.nullable(false))
                    .column(NAME, SQLDataType.VARCHAR.nullable(false))
                    .constraints(
                            DSL.constraint("pk_categories").primaryKey(ID),
                            DSL.constraint("uq_categories_name").unique(NAME)
                    )
                    .execute();
            return null;
        });
    }

    @Override
    public Category save(Category category) {
        withContext(ctx -> {
            ctx.insertInto(CATEGORIES, ID, NAME)
                    .values(category.getId(), category.getName())
                    .onConflict(ID)
                    .doUpdate()
                    .set(NAME, category.getName())
                    .execute();
            return null;
        });

        return category;
    }

    @Override
    public List<Category> findAll() {
        return withContext(ctx ->
                ctx.select(ID, NAME)
                        .from(CATEGORIES)
                        .orderBy(ID)
                        .fetch(this::mapCategory)
        );
    }

    private Category mapCategory(Record record) {
        Long id = record.get(ID);
        String name = record.get(NAME);

        return new Category(id, name);
    }

    private <T> T withContext(JooqOperation<T> operation) {
        try (Connection connection = DriverManager.getConnection(url)) {
            DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
            return operation.execute(ctx);
        } catch (Exception e) {
            throw new RuntimeException("jOOQ category database operation failed", e);
        }
    }

    @FunctionalInterface
    private interface JooqOperation<T> {
        T execute(DSLContext ctx);
    }
}
