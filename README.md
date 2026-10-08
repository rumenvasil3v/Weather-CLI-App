# TODO-Rest-API

A small JSON API for a to-do list, written with Javalin and SQLite. Todos can be listed, filtered and read, and each one can have comments.

Java 21, Maven. Port 8000. The project itself is in `yatl/yatl`.

## Before you start it

`Main.java` points at the database with an absolute Windows path from my own machine. Change `databasePath` to wherever `src/main/resources/database.db` sits on your computer, then run `com.yatl.Main` from your IDE. On start it applies the Flyway migration for the comments table and begins listening.

The database that ships in the repo already has four todos in it. There's no endpoint to create, edit or delete a todo yet; `Seeder` is what resets the table to the four sample rows.

## Endpoints

### GET /todos

All todos. Add `?status=active` or `?status=completed` to filter.

```
curl "localhost:8000/todos?status=completed"
```

### GET /todos/{id}

One todo, or a 404 with "Todo item not found".

### GET /todos/{id}/comments

The comments on a todo, as a JSON object of comment id to comment text.

### POST /todos/{id}/comments

Adds a comment. The text goes in as a form field called `content`, not as JSON.

```
curl -X POST -d "content=Remember to do this first" localhost:8000/todos/1/comments
```

Returns 201 with the new comment's id. You get a 404 if the todo doesn't exist and a 400 if `content` is missing.

### DELETE /comments/{id}

Removes one comment. 200 on success, 404 if there's no such comment.

## Under the hood

- Javalin 7 for routing, Jackson for JSON, `sqlite-jdbc` for storage, Flyway for the migration
- `TodoDao` does the SQL, `TodoController` handles requests and status codes, and `AppConfig` wires the routes
- Comments are deleted automatically when their todo goes (`ON DELETE CASCADE`)

## Tests

There are 19 tests across the model, DAO, controller and an integration suite, using JUnit 5, Mockito and REST Assured.

```
cd yatl/yatl
mvn test
```
