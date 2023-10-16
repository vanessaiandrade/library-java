package com.vanessaiandrade.library.controllers;

import com.vanessaiandrade.library.data.ConnectionManager;
import com.vanessaiandrade.library.models.Reader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReaderMgmtViewController {
    // <editor-fold desc="Vars">

    private static ReaderMgmtViewController instance;
    private final ConnectionManager connectionManager = ConnectionManager.getInstance();

    // </editor-fold>

    // <editor-fold desc="Constructor">

    private ReaderMgmtViewController() {

    }

    public static ReaderMgmtViewController getInstance() {
        if (instance == null) {
            instance = new ReaderMgmtViewController();
        }
        return instance;
    }

    // </editor-fold>

    // <editor-fold desc="Persistence">

    public boolean addReader(Reader reader) {
        if (existsReader(reader, false)) {
            return false;
        }

        try {
            String query = "INSERT INTO readers (first_name, last_name, birth_date, loan_enabled) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = null;

            preparedStatement = connectionManager.getConnection().prepareStatement(query);

            preparedStatement.setString(1, reader.getFirstName());
            preparedStatement.setString(2, reader.getLastName());
            preparedStatement.setString(3, reader.getDateOfBirth());
            preparedStatement.setBoolean(4, reader.isLoanEnabled());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;

    }

    public boolean updateReader(Reader reader) {
        if (existsReader(reader, true)) {
            return false;
        }

        try {
            String query = "UPDATE readers SET first_name = ?, last_name = ?, birth_date = ?, loan_enabled = ? WHERE readers_id = ?";

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            preparedStatement.setString(1, reader.getFirstName());
            preparedStatement.setString(2, reader.getLastName());
            preparedStatement.setString(3, reader.getDateOfBirth());
            preparedStatement.setBoolean(4, reader.isLoanEnabled());
            System.out.println(reader.isLoanEnabled());
            preparedStatement.setInt(5, reader.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    // </editor-fold>

    // <editor-fold desc="Helpers">

    public boolean existsReader(Reader reader, boolean verifyWithId) {
        try {
            String query = "SELECT COUNT(readers_id) AS countReaders FROM readers WHERE first_name = ? AND last_name = ? AND birth_date = ?" + ((verifyWithId) ? " AND readers_id <> ?" : "");

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            preparedStatement.setString(1, reader.getFirstName());
            preparedStatement.setString(2, reader.getLastName());
            preparedStatement.setString(3, reader.getDateOfBirth());
            if (verifyWithId) {
                preparedStatement.setInt(4, reader.getId());
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return (resultSet.getInt("countReaders") > 0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    // </editor-fold>

}


