package com.vanessaiandrade.library.models;

public class Book {


    // <editor-fold desc="Vars">

    private int id;
    private String title;
    private int totalAmount;
    private int availableForLoan;

    // </editor-fold>


    // <editor-fold desc="Constructor">

    public Book(int id, String title, int totalAmount, int availableForLoan) {
        this.id = id;
        this.title = title;
        this.totalAmount = totalAmount;
        this.availableForLoan = availableForLoan;
    }

    // </editor-fold>


    // <editor-fold desc="Helpers">

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getAvailableForLoan() {
        return availableForLoan;
    }

    @Override
    public String toString() {
        return this.title;
    }
    // </editor-fold>
}
