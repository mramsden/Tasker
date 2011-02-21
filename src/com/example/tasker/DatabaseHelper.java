package com.example.tasker;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of
 * the tasker database.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    
    /**
     * Name of the database file for tasker.
     */
    private static final String DATABASE_NAME = "tasker.db";

    /**
     * The current database version number.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * This is the DAO used to access the tasks table
     */
    private Dao<Task, Integer> taskDao = null;

    /**
     * This is called when the database is first created.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Task.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when the application is upgraded and it has a higher
     * version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Task.class, true);
            onCreate(db);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our Tasks class.
     */
    public Dao<Task, Integer> getTaskDao() throws SQLException {
        if (taskDao == null) {
            taskDao = BaseDaoImpl.createDao(getConnectionSource(), Task.class);
        }
        return taskDao;
    }

    @Override
    public void close() {
        super.close();
        taskDao = null;
    }

}
