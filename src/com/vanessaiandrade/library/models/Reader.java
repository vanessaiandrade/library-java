package com.vanessaiandrade.library.models;

public class Reader {
    // <editor-fold desc="Vars">
    private int id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private boolean loanEnabled;

        // </editor-fold>


    // <editor-fold desc="Constructor">


    public Reader(int id, String firstName, String lastName, String dateOfBirth, Boolean loanEnabled) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.loanEnabled = loanEnabled;
    }

    // </editor-fold>


    // <editor-fold desc="Helpers">

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isLoanEnabled() {
        return loanEnabled;
    }

    public void setLoanEnabled(boolean loanEnabled) {
        this.loanEnabled = loanEnabled;
    }

    @Override
    public String toString() {

        return firstName + " " + lastName + "       Birth: " + dateOfBirth;
    }
    // </editor-fold>
}
