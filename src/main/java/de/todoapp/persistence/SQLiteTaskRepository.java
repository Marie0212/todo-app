package de.todoapp.persistence;

import de.todoapp.domain.Task;
import de.todoapp.domain.TaskStatus;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SQLiteTaskRepository implements TaskWriter, TaskReader, TaskUpdater, TaskDeleter {

    private static final Table<?> TASKS = DSL.table(DSL.name("tasks"));

    private static final Field<Long> ID = DSL.field(DSL.name("id"), Long.class);
    private static final Field<String> TITLE = DSL.field(DSL.name("title"), String.class);
    private static final Field<String> DESCRIPTION = DSL.field(DSL.name("description"), String.class);
    private static final Field<String> DUE_DATE = DSL.field(DSL.name("due_date"), String.class);
    private static final Field<String> STATUS = DSL.field(DSL.name("status"), String.class);
    private static final Field<String> CATEGORY_ID = DSL.field(DSL.name("category_id"), String.class);

    private final String url;

    public SQLiteTaskRepository(String url) {
        this.url = url;
        System.out.println(">>> SQLiteTaskRepository AKTIV: " + url);
        init();
    }

    private void init() {
        withContext(ctx -> {
            ctx.createTableIfNotExists(TASKS)
                    .column(ID, SQLDataType.BIGINT.nullable(false))
                    .column(TITLE, SQLDataType.VARCHAR.nullable(false))
                    .column(DESCRIPTION, SQLDataType.VARCHAR)
                    .column(DUE_DATE, SQLDataType.VARCHAR)
                    .column(STATUS, SQLDataType.VARCHAR.nullable(false))
                    .column(CATEGORY_ID, SQLDataType.VARCHAR)
                    .constraints(DSL.constraint("pk_tasks").primaryKey(ID))
                    .execute();
            return null;
        });
    }

    @Override
    public Task save(Task task) {
        withContext(ctx -> {
            ctx.insertInto(TASKS, ID, TITLE, DESCRIPTION, DUE_DATE, STATUS, CATEGORY_ID)
                    .values(
                            task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            toDbDate(task.getDueDate()),
                            task.getStatus().name(),
                            task.getCategory()
                    )
                    .onConflict(ID)
                    .doUpdate()
                    .set(TITLE, task.getTitle())
                    .set(DESCRIPTION, task.getDescription())
                    .set(DUE_DATE, toDbDate(task.getDueDate()))
                    .set(STATUS, task.getStatus().name())
                    .set(CATEGORY_ID, task.getCategory())
                    .execute();
            return null;
        });

        return task;
    }

    @Override
    public List<Task> findAll() {
        return withContext(ctx ->
                ctx.select(ID, TITLE, DESCRIPTION, DUE_DATE, STATUS, CATEGORY_ID)
                        .from(TASKS)
                        .orderBy(ID)
                        .fetch(this::mapTask)
        );
    }

    @Override
    public Optional<Task> update(Task task) {
        int affected = withContext(ctx ->
                ctx.update(TASKS)
                        .set(TITLE, task.getTitle())
                        .set(DESCRIPTION, task.getDescription())
                        .set(DUE_DATE, toDbDate(task.getDueDate()))
                        .set(STATUS, task.getStatus().name())
                        .set(CATEGORY_ID, task.getCategory())
                        .where(ID.eq(task.getId()))
                        .execute()
        );

        return affected > 0 ? Optional.of(task) : Optional.empty();
    }

    @Override
    public Optional<Long> deleteById(long id) {
        int affected = withContext(ctx ->
                ctx.deleteFrom(TASKS)
                        .where(ID.eq(id))
                        .execute()
        );

        return affected > 0 ? Optional.of(id) : Optional.empty();
    }

    private Task mapTask(Record record) {
        Long id = record.get(ID);
        String title = record.get(TITLE);
        String description = record.get(DESCRIPTION);

        String due = record.get(DUE_DATE);
        LocalDate dueDate = (due == null || due.isBlank()) ? null : LocalDate.parse(due);

        String statusValue = record.get(STATUS);
        TaskStatus status = (statusValue == null || statusValue.isBlank())
                ? TaskStatus.OPEN
                : TaskStatus.valueOf(statusValue);

        String category = record.get(CATEGORY_ID);

        return new Task(id, title, description, dueDate, status, category);
    }

    private String toDbDate(LocalDate date) {
        return date == null ? null : date.toString();
    }

    private <T> T withContext(JooqOperation<T> operation) {
        try (Connection connection = DriverManager.getConnection(url)) {
            DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
            return operation.execute(ctx);
        } catch (Exception e) {
            throw new RuntimeException("jOOQ task database operation failed", e);
        }
    }

    @FunctionalInterface
    private interface JooqOperation<T> {
        T execute(DSLContext ctx);
    }
}
