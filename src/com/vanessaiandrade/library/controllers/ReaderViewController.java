package com.vanessaiandrade.library.controllers;

import com.vanessaiandrade.library.data.ConnectionManager;
import com.vanessaiandrade.library.models.Reader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ReaderViewController {


    // <editor-fold desc="Vars">

    private static ReaderViewController instance;
    private final ConnectionManager connectionManager = ConnectionManager.getInstance();

    // </editor-fold>


    // <editor-fold desc="Constructor">

    private ReaderViewController() {

    }

    public static ReaderViewController getInstance() {
        if (instance == null) {
            instance = new ReaderViewController();
        }
        return instance;
    }

    // </editor-fold>


    // <editor-fold desc="Helpers">
    public Vector<Reader> getAllReaders() {
        try {
            String query = "SELECT * FROM readers";

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            Vector<Reader> readers = new Vector<>();

            while (resultSet.next()) {
                readers.add(new Reader(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getBoolean(5)));
            }
            return readers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Vector<Reader> searchReader(Reader reader) {
        try {
            String query = getQuery(reader.getFirstName(), reader.getLastName(), reader.getDateOfBirth());

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            Vector<Reader> readers = new Vector<>();

            while (resultSet.next()) {
                readers.add(new Reader(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getBoolean(5)));
            }

            return readers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Reader getReaderById(int id) {
        try {
            String query = "SELECT * FROM readers WHERE readers_id = ?";

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return new Reader(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getBoolean(5));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getQuery(String readerFirstName, String readerLastName, String readerBirthDate) {

        String query = "SELECT * FROM readers WHERE ";
        if (readerFirstName != null && !readerFirstName.isEmpty()) {
            query += " first_name LIKE '%" + readerFirstName + "%'";
        }
        if (readerLastName != null && !readerLastName.isEmpty()) {
            query += " OR last_name LIKE '%" + readerLastName + "%'";
        }
        if (readerBirthDate != null && !readerBirthDate.isEmpty()) {
            query += " OR birth_date LIKE '%" + readerBirthDate + "%'";
        }
        return query;
    }

    // </editor-fold>


}

