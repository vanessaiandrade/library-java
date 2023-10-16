package com.vanessaiandrade.library.controllers;


import com.vanessaiandrade.library.data.ConnectionManager;
import com.vanessaiandrade.library.models.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMgmtViewController {
    // <editor-fold desc="Vars">

    private static BookMgmtViewController instance;
    private final ConnectionManager connectionManager = ConnectionManager.getInstance();

    // </editor-fold>


    // <editor-fold desc="Constructor">

    private BookMgmtViewController() {

    }

    public static BookMgmtViewController getInstance() {
        if (instance == null) {
            instance = new BookMgmtViewController();
        }
        return instance;
    }
    // </editor-fold>


    // <editor-fold desc="Persistence">
    public boolean addBook(Book book) {
        if (existsBook(book, false)) {
            return false;
        }

        try {
            String query = "INSERT INTO books (title, amount, available_for_loan) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setInt(2, book.getTotalAmount());
            preparedStatement.setInt(3, book.getAvailableForLoan());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public boolean updateBook(Book book) {
        if (existsBook(book, true)) {
            return false;
        }
        if (book.getTotalAmount() >= existsLoan(book)) {
            try {

                String query = "UPDATE books SET title = ?, amount = ?, available_for_loan = ? WHERE books_id = ?";

                PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
                preparedStatement.setString(1, book.getTitle());
                preparedStatement.setInt(2, book.getTotalAmount());
                preparedStatement.setInt(3, (book.getTotalAmount() - existsLoan(book)));
                preparedStatement.setInt(4, book.getId());
                preparedStatement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return true;
        } else {
            return false;
        }


    }


    // </editor-fold>


    // <editor-fold desc="Helpers">
    private boolean existsBook(Book book, boolean verifyWithId) {
        try {
            String query = "SELECT COUNT(books_id) AS countBooks FROM books WHERE title = ?" + ((verifyWithId) ? " AND books_id <> ?" : "");

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            preparedStatement.setString(1, book.getTitle());
            if (verifyWithId) {
                preparedStatement.setInt(2, book.getId());
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return (resultSet.getInt("countBooks") > 0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int existsLoan(Book book) {
        try {
            String query = "SELECT COUNT(book_id) AS countCurrentLoans FROM loans WHERE book_id = ? AND return_date IS NULL";
            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, book.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            return resultSet.getInt("countCurrentLoans");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // </editor-fold>

}
