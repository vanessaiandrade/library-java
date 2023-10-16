package com.vanessaiandrade.library.controllers;

import com.vanessaiandrade.library.data.ConnectionManager;
import com.vanessaiandrade.library.models.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class BookViewController {

    // <editor-fold desc="Vars">

    private static BookViewController instance;
    private final ConnectionManager connectionManager = ConnectionManager.getInstance();

    // </editor-fold>


    // <editor-fold desc="Constructor">

    private BookViewController() {

    }

    public static BookViewController getInstance() {
        if (instance == null) {
            instance = new BookViewController();
        }
        return instance;
    }

    // </editor-fold>


    // <editor-fold desc="Helpers">

    public Vector<Book> getAllBooks() {
        try {
            String query = "SELECT * FROM books";

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            Vector<Book> books = new Vector<Book>();

            while (resultSet.next()) {


                books.add(new Book(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4)));
            }

            return books;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Vector<Book> searchBook(Book book) {
        try {
            String query = "SELECT * FROM books WHERE title LIKE ?";

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            preparedStatement.setString(1, ("%" + book.getTitle() + "%"));
            ResultSet resultSet = preparedStatement.executeQuery();

            Vector<Book> books = new Vector<Book>();

            while (resultSet.next()) {
                books.add(new Book(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4)));
            }

            return books;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Book getBookById(int id) {
        try {
            String query = "SELECT * FROM books WHERE books_id = ?";

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return new Book(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // </editor-fold>

}
