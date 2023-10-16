package com.vanessaiandrade.library.views;


import com.vanessaiandrade.library.controllers.BookMgmtViewController;
import com.vanessaiandrade.library.models.Book;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookManagementView {

    // <editor-fold desc="Vars">
    private static BookManagementView instance;
    private final BookMgmtViewController bookMgmtViewController = BookMgmtViewController.getInstance();
    private JPanel jPanelBookMgmtView;
    private JLabel labelBookTitle;
    private JLabel labelSpinnerTotalAmount;
    private JTextField txtBookTitle;
    private JSpinner spinnerTotalAmount;
    private JButton btnCancel;
    private JButton btnAddOrUpdate;
    private Book book;

    // </editor-fold>

    // <editor-fold desc="Constructor">

    private BookManagementView() {
        initView();
    }

    public static BookManagementView getInstance() {
        if (instance == null) {
            instance = new BookManagementView();
            return instance;
        }

        return instance;
    }

    private void initView() {
        spinnerChangeListener();
        btnCancelClickEvent();
        btnAddOrUpdateClickEvent();

    }
    // </editor-fold>


    // <editor-fold desc="Events">

    private void spinnerChangeListener() {
        spinnerTotalAmount.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (int) spinnerTotalAmount.getValue();
                if (value < 0) {
                    setSpinner(0);
                }
            }
        });
    }

    private void btnCancelClickEvent() {
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
                MainView.getInstance().getTabPane().setSelectedIndex(0);
            }
        });
    }

    private void btnAddOrUpdateClickEvent() {
        btnAddOrUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (book == null) {
                    addBook();
                } else {
                    updateBook();
                }
            }
        });
    }


    // </editor-fold>


    // <editor-fold desc="Persistence">

    private void addBook() {
        String title = txtBookTitle.getText().trim();
        int totalAmount = (int) spinnerTotalAmount.getValue();


        boolean wasInserted = false;
        String resultMessage = "";

        if (title.isEmpty() && totalAmount == 0) {
            resultMessage = "Title and number of books is mandatory.";
        } else if (title.isEmpty()) {
            resultMessage = "Book title required.";
        } else if (totalAmount == 0) {
            resultMessage = "The number of books is a mandatory requirement.";
        } else {
            Book book = new Book(0, title, totalAmount, totalAmount);

            wasInserted = bookMgmtViewController.addBook(book);

            if (wasInserted) {
                resultMessage = "Successful Book Add!\nBook: " + title + "\nAmount: " + totalAmount;
            } else {
                resultMessage = "This book is already available, please use the Update Books.";
            }

        }
        messageBuilder((wasInserted), resultMessage);
        resetFields();
        MainView.getInstance().getTabPane().setSelectedIndex(0);
    }

    private void updateBook() {
        String title = txtBookTitle.getText().trim();
        int amount = (int) spinnerTotalAmount.getValue();

        boolean wasUpdated = false;
        String resultMessage = "";

        if (title.isEmpty()) {
            resultMessage = "Book title required.";
        } else {
            book.setTitle(title);
            book.setTotalAmount(amount);

            wasUpdated = bookMgmtViewController.updateBook(book);

            if (wasUpdated) {
                resultMessage = "Book: " + title + "\nTotal number of copies: " + amount;
            } else {
                resultMessage = "Unable to update book, check if book already exists or has open loans";
            }
        }
        messageBuilder((wasUpdated), resultMessage);
        resetFields();
        MainView.getInstance().getTabPane().setSelectedIndex(0);

    }


    // </editor-fold>


    // <editor-fold desc="Helpers">
    public JPanel getJPanel() {
        return jPanelBookMgmtView;
    }

    public void initBookUpdate(Book book) {
        this.book = book;
        txtBookTitle.setText(book.getTitle());
        setSpinner(book.getTotalAmount());
        setBtnTxtAddOrUp("Update");
    }

    private void setSpinner(int value) {
        spinnerTotalAmount.setValue(value);
    }

    private void setBtnTxtAddOrUp(String txt) {
        btnAddOrUpdate.setText(txt);
    }

    private void resetFields() {
        book = null;
        txtBookTitle.setText(null);
        setSpinner(0);
        setBtnTxtAddOrUp("Insert");

        MainView.getInstance().refreshAllTabs();

    }

    private void messageBuilder(boolean wasInserted, String resultMessage) {
        String dialogTitle = (wasInserted) ? "SUCCESS" : "ERROR";
        int messageType = (wasInserted) ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(null, resultMessage, dialogTitle, messageType);
    }


    // </editor-fold>


}
