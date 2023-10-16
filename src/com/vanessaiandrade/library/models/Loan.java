package com.vanessaiandrade.library.models;


public class Loan {
    // <editor-fold desc="Vars">

    private int id;
    private Book book;
    private Reader reader;
    private String loanDate;
    private String returnDate;

    // </editor-fold>


    // <editor-fold desc="Constructor">


    public Loan(int id, Book book, Reader reader, String loanDate, String returnDate) {
        this.id = id;
        this.book = book;
        this.reader = reader;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }
    // </editor-fold>


    // <editor-fold desc="Helpers">


    public int getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Reader getReader() {
        return reader;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    // </editor-fold>
}
