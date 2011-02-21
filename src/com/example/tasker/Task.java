package com.example.tasker;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * This class represents a task. It is responsible for
 * interfacing with the database using ORMLite.
 */
@DatabaseTable(tableName="tasks")
public class Task {
    
    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private String title;
    @DatabaseField
    private String body;

    public Task(String title, String body) {
        this(title);
        this.body = body;
    }

    public Task(String title) {
        this.title = title;
    }

    /**
     * This is the default constructor. This default constructor
     * is required by ORMLite.
     */
    public Task() {

    }

    /**
     * Returns the id of the task. This will be null
     * if the object does not exist in the database
     * yet.
     *
     * @return The object id.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the title of the task.
     *
     * @return The task title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the task.
     *
     * @param title The new title for the task.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the body of the task.
     *
     * @return The task body
     */
    public void getBody() {
        return body;
    }

    /**
     * Sets the body of the task.
     *
     * @param body The new body for the task.
     */
    public String setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=").append(id);
        sb.append(", ").append("title=").append(title);
        sb.append(", ").append("body=").append(body);
        return sb.toString();
    }

}
