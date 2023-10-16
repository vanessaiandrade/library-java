package com.vanessaiandrade.library.controllers;

import com.vanessaiandrade.library.data.ConnectionManager;
import com.vanessaiandrade.library.models.Book;
import com.vanessaiandrade.library.models.Loan;
import com.vanessaiandrade.library.models.Reader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class LoanMgmtViewController {
    // <editor-fold desc="Vars">

    private static LoanMgmtViewController instance;
    private final ConnectionManager connectionManager = ConnectionManager.getInstance();

    // </editor-fold>


    // <editor-fold desc="Constructor">

    private LoanMgmtViewController() {

    }

    public static LoanMgmtViewController getInstance() {
        if (instance == null) {
            instance = new LoanMgmtViewController();
        }
        return instance;
    }

    // </editor-fold>


    // <editor-fold desc="Persistence">
    public boolean addLoan(Loan loan) {
        if (existsLoan(loan, false)) {
            return false;
        } else {
            try {
                String query = "INSERT INTO loans (book_id, reader_id, loan_date, return_date) VALUES (?, ?, ?, ?)";

                PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
                preparedStatement.setInt(1, loan.getBook().getId());
                preparedStatement.setInt(2, loan.getReader().getId());
                preparedStatement.setString(3, loan.getLoanDate());
                preparedStatement.setString(4, loan.getReturnDate());
                preparedStatement.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return true;
        }
    }

    public boolean updateLoan(Loan loan) {
        try {
            String query = "UPDATE loans SET return_date = ? WHERE loan_id = ?";

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            preparedStatement.setString(1, loan.getReturnDate());
            preparedStatement.setInt(2, loan.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;

    }

    public boolean deleteLoan(Loan loan) {
        try {
            String query = "DELETE FROM loans WHERE loan_id = ?";
            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, loan.getId());
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void updateBookAvailability(Book book, int changeAmount) {
        String query = "UPDATE books SET available_for_loan = available_for_loan + ? WHERE books_id = ?";
        try {
            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, changeAmount);
            preparedStatement.setInt(2, book.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // </editor-fold>


    // <editor-fold desc="Helpers">
    public ArrayList<Book> getAllBooksAvailableForLoans() {

        try {
            String query = "SELECT * FROM books WHERE available_for_loan > 0";

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Book> booksList = new ArrayList<>();

            while (resultSet.next()) {
                booksList.add(new Book(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4)));

            }
            return booksList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Book> searchBookAvailable(String booksTitle) {

        try {
            String query = "SELECT * FROM books WHERE available_for_loan > 0 AND books.title LIKE '%" + booksTitle + "%' ";

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Book> booksList = new ArrayList<>();

            while (resultSet.next()) {
                booksList.add(new Book(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4)));

            }
            return booksList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Reader> getAllReaderEnableForLoans() {

        try {
            String query = "SELECT * FROM readers WHERE loan_enabled > 0";

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Reader> readerList = new ArrayList<>();

            while (resultSet.next()) {
                readerList.add(new Reader(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getBoolean(5)));

            }
            return readerList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Reader> searchReaderForLoan(String searchParameter) {

        try {
            String query = "SELECT * FROM readers WHERE loan_enabled > 0 AND first_name LIKE '%" + searchParameter + "%' OR  last_name LIKE '%" + searchParameter + "%' OR birth_date LIKE '%" + searchParameter + "%'";

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Reader> readersList = new ArrayList<>();

            while (resultSet.next()) {
                readersList.add(new Reader(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getBoolean(5)));

            }
            return readersList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean existsLoan(Loan loan, boolean verifyWithId) {
        try {
            String query = "SELECT COUNT(loan_id) AS countLoans FROM loans WHERE book_id = ? AND reader_id = ? AND return_date IS NULL" + ((verifyWithId) ? " AND loan_id <> ?" : "");

            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, loan.getBook().getId());
            preparedStatement.setInt(2, loan.getReader().getId());
            if (verifyWithId) {
                preparedStatement.setInt(2, loan.getId());
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return (resultSet.getInt("countLoans") > 0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // </editor-fold>

}

