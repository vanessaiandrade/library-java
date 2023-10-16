package com.vanessaiandrade.library.controllers;

import com.vanessaiandrade.library.data.ConnectionManager;
import com.vanessaiandrade.library.models.Book;
import com.vanessaiandrade.library.models.Loan;
import com.vanessaiandrade.library.models.Reader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class LoanViewController {
    // <editor-fold desc="Vars">

    private static LoanViewController instance;
    private final ConnectionManager connectionManager = ConnectionManager.getInstance();

    // </editor-fold>


    // <editor-fold desc="Constructor">

    private LoanViewController() {

    }

    public static LoanViewController getInstance() {
        if (instance == null) {
            instance = new LoanViewController();
        }
        return instance;
    }


    // </editor-fold>


    // <editor-fold desc="Helpers">
    public Vector<Loan> getAllLoans() {
        try {
            String query = "SELECT * FROM loans INNER JOIN books ON loans.book_id = books.books_id INNER JOIN readers ON loans.reader_id = readers.readers_id";


            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            Vector<Loan> loans = new Vector<>();

            while (resultSet.next()) {
                Reader reader = new Reader(resultSet.getInt("reader_id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name"), resultSet.getString("birth_date"),
                        resultSet.getBoolean("loan_enabled"));

                Book book = new Book(resultSet.getInt("book_id"), resultSet.getString("title"),
                        resultSet.getInt("amount"), resultSet.getInt("available_for_loan"));

                Loan loan = new Loan(resultSet.getInt("loan_id"), book, reader, resultSet.getString("loan_date"), resultSet.getString("return_date"));

                loans.add(loan);
            }
            return loans;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Loan getLoanById(int id) {
        try {
            String query = "SELECT * FROM loans INNER JOIN books ON loans.book_id = books.books_id INNER JOIN readers ON loans.reader_id = readers.readers_id WHERE loan_id = ?";

            PreparedStatement preparedStatement = null;

            preparedStatement = connectionManager.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            Reader reader = new Reader(resultSet.getInt("reader_id"), resultSet.getString("first_name"),
                    resultSet.getString("last_name"), resultSet.getString("birth_date"),
                    resultSet.getBoolean("loan_enabled"));

            Book book = new Book(resultSet.getInt("book_id"), resultSet.getString("title"),
                    resultSet.getInt("amount"), resultSet.getInt("available_for_loan"));

            return new Loan(resultSet.getInt(1), book, reader, resultSet.getString(4), resultSet.getString(5));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Vector<Loan> searchLoan(Loan loan, String searchTxt, String startLoanDate, String endLoanDate,
                                   boolean wasReturned) {
        try {
            String query = getQuery(searchTxt, startLoanDate, endLoanDate, wasReturned);
            if (query.endsWith("WHERE")) {
                return getAllLoans();
            }
            PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            Vector<Loan> loans = new Vector<>();

            while (resultSet.next()) {
                Reader reader = new Reader(resultSet.getInt("reader_id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name"), resultSet.getString("birth_date"),
                        resultSet.getBoolean("loan_enabled"));

                Book book = new Book(resultSet.getInt("book_id"), resultSet.getString("title"),
                        resultSet.getInt("amount"), resultSet.getInt("available_for_loan"));

                loans.add(new Loan(resultSet.getInt(1), book, reader, resultSet.getString(4), resultSet.getString(5)));
            }
            return loans;


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);

        }


    }


    private String getQuery(String txtSearch, String
            startLoanDate, String endLoanDate, boolean openLoan) {

        String query = "SELECT * FROM loans INNER JOIN books ON loans.book_id = books.books_id INNER JOIN readers ON loans.reader_id = readers.readers_id WHERE";

        if (openLoan) {
            query += " loans.return_date IS NULL";
        }
        if (startLoanDate != null && !startLoanDate.isEmpty()) {
            if (endLoanDate != null && !endLoanDate.isEmpty()) {
                if (openLoan) {
                    query += " AND ";
                } else {
                    query += " loans.loan_date BETWEEN '" + startLoanDate + "%' AND '" + endLoanDate + "%'";
                }
            }
        }
        if (txtSearch != null && !txtSearch.isEmpty()) {
            query += (query.endsWith("WHERE") ? "" : " AND") +
                    "(books.title LIKE '%" + txtSearch + "%' OR readers.first_name LIKE '%" + txtSearch + "%' OR readers.last_name LIKE '%" + txtSearch + "%')";
        }

        return query;
    }


    // </editor-fold>
}
