package io.bytechannel.sdb;

import io.bytechannel.sdb.types.Database;
import io.bytechannel.sdb.types.Table;
import lombok.*;

import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Logger;

public class SimpleDatabase {


    @Getter
    private LinkedList<Database> databases;
    @Getter
    private int port;
    @Getter
    private String username, hostname, password, database;
    @Getter
    private Connection connection;
    @Getter
    private final Logger logger = Logger.getLogger("SimpleDatabase");
    @Getter
    private boolean isConnected = connection != null;

    public SimpleDatabase(final String hostname, final int port, final String username, final String password) {
        this.username = username;
        this.hostname = hostname;
        this.port = port;
        this.password = password;
        this.connect();
        this.preInit();
    }

    public SimpleDatabase(final String hostname, final int port, final String username, final String password, final String database) {
        this.username = username;
        this.hostname = hostname;
        this.port = port;
        this.password = password;
        this.database = database;
        this.connect();
        this.preInit();
    }


    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + (this.database != null ? "/" + this.database : "/mysql"), username, password);
            this.logger.info("Successfully connected to MySQL.");
        }catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public void close() {
        try {
            if(!this.isConnected) return;
            this.connection.close();
            this.logger.info("Successfully disconnected from MySQL.");
        }catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void preInit() {
        this.databases = new LinkedList<>();
    }

    public void postInit() {
        if(this.databases.isEmpty()) return;
        this.databases.forEach(all -> {
            sql("CREATE DATABASE IF NOT EXISTS ", all.getName());
            sql("USE " + all.getName());

            for (Table table : all.getTables()) {
                final StringBuilder stringBuilder = new StringBuilder();
                table.getColumns().forEach(acolumn -> {
                    stringBuilder
                            .append(acolumn.getName())
                            .append(" ")
                            .append(acolumn.getData());
                });
                sql("CREATE TABLE IF NOT EXISTS " + table.getName() + " (" + stringBuilder + ")");
            }


        });

    }


    public void addColumn(final Table table, final String column, final Object value) {
        sql("INSERT INTO " + table.getName() + " (" + column + ") VALUES ('" + value + "')");

    }

    public void updateColumn(final Table table, final String column, final Object data, final Object value, final Object newValue) {
        sql("UPDATE " + table.getName() + " SET " + column + "='" + newValue + "' WHERE " + data + "='" + value + "'");
    }



    public Object getColumn(final Table table, final Object column, final String data, final Object value) {
        try {
            final String query = "SELECT " + column + " FROM " + table.getName() + " WHERE " + data + "='" + value + "'";

            final ResultSet resultSet = sqlResult(query);
            if (resultSet.next()) {
                return resultSet.getObject(String.valueOf(column));
            }
        }catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;

    }

    public boolean columnExists(final Table table, final String column, final String data) {
        return this.getColumn(table, column, column, data) == null;
    }


    public void addDatabase(final Database database) {
        this.databases.add(database);
    }


    private void sql(final String... query) {
        try {
            final Statement statement = this.connection.createStatement();
            final StringBuilder stringBuilder = new StringBuilder();
            for (String s : query) stringBuilder.append(s);

            stringBuilder.append(";");
            statement.executeUpdate(stringBuilder.toString());
            statement.close();

        }catch (SQLException exception) {
            exception.printStackTrace();;
        }
    }

    public ResultSet sqlResult(String sql) throws SQLException {
        final Statement statement = this.connection.createStatement();
        return statement.executeQuery(sql);
    }


}
