package com.vanessaiandrade.library.views;

import com.vanessaiandrade.library.controllers.BookViewController;
import com.vanessaiandrade.library.controllers.LoanMgmtViewController;
import com.vanessaiandrade.library.controllers.ReaderViewController;
import com.vanessaiandrade.library.models.Book;
import com.vanessaiandrade.library.models.Loan;
import com.vanessaiandrade.library.models.Reader;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LoanManagementView {

    // <editor-fold desc="Vars">
    private static LoanManagementView instance;
    private final LoanMgmtViewController loanMgmtViewController = LoanMgmtViewController.getInstance();
    private JPanel jPanelLoanMgmtView;
    private JPanel jPanelBookAndReader;
    private JPanel jPanelBookFilter;
    private JPanel jPanelReaderFilter;
    private JPanel jPanelLoan;
    private JLabel labelLoanDate;
    private JLabel labelReturnDate;
    private JTextField txtBookTitle;
    private JButton btnClearBook;
    private JComboBox cmbBoxBookList;
    private JTextField txtReader;
    private JButton btnClearReader;
    private JComboBox cmbBoxReaderList;
    private JTextField txtLoanDate;
    private JTextField txtReturnDate;
    private JButton btnCancel;
    private JButton btnAddOrUpdate;
    private JButton btnDelete;
    private Loan loan;
    private Book book;
    private Reader reader;

    // </editor-fold>

    // <editor-fold desc="Constructor">
    private LoanManagementView() {
        initView();

    }

    public static LoanManagementView getInstance() {
        if (instance == null) {
            instance = new LoanManagementView();
            return instance;
        }

        return instance;
    }

    private void initView() {
        //Book Panel
        txtBookChangeListener();
        btnClearBookClickEvent();
        cmbBoxBooksListItemListener();
        updateBookList(LoanMgmtViewController.getInstance().getAllBooksAvailableForLoans());

        //Reader Panel
        txtReaderChangeListener();
        btnClearReaderClickEvent();
        cmbBoxReadersListItemListener();
        updateReaderList(LoanMgmtViewController.getInstance().getAllReaderEnableForLoans());

        //Loan Panel
        txtLoanDate.setText(getDate());
        btnCancelClickEvent();
        btnDeleteClickEvent();
        btnAddOrUpdateClickEvent();
    }

    // </editor-fold>


    // <editor-fold desc="Events">

    private void txtBookChangeListener() {
        txtBookTitle.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (loan == null) {
                    if (txtBookTitle.getText().isEmpty()) {
                        updateBookList(loanMgmtViewController.getAllBooksAvailableForLoans());
                    } else {
                        updateBookList(loanMgmtViewController.searchBookAvailable(txtBookTitle.getText()));
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (loan == null) {
                    if (txtBookTitle.getText().isEmpty()) {
                        updateBookList(loanMgmtViewController.getAllBooksAvailableForLoans());
                    } else {
                        updateBookList(loanMgmtViewController.searchBookAvailable(txtBookTitle.getText()));
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

    }

    private void btnClearBookClickEvent() {
        btnClearBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTxtField(txtBookTitle, null);
            }
        });
    }

    private void cmbBoxBooksListItemListener() {
        cmbBoxBookList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cmbBoxBookList.getSelectedItem();
            }
        });
    }

    private void txtReaderChangeListener() {
        txtReader.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (loan == null) {
                    if (txtReader.getText().isEmpty()) {
                        updateReaderList(loanMgmtViewController.getAllReaderEnableForLoans());
                    } else {
                        updateReaderList(loanMgmtViewController.searchReaderForLoan(txtReader.getText()));
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (loan == null) {
                    if (txtReader.getText().isEmpty()) {
                        updateReaderList(loanMgmtViewController.getAllReaderEnableForLoans());
                    } else {
                        updateReaderList(loanMgmtViewController.searchReaderForLoan(txtReader.getText()));
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

    }

    private void btnClearReaderClickEvent() {
        btnClearReader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTxtField(txtReader, null);
            }
        });
    }

    private void cmbBoxReadersListItemListener() {
        cmbBoxReaderList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cmbBoxReaderList.getSelectedItem();
            }
        });
    }

    private void btnCancelClickEvent() {
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
                MainView.getInstance().getTabPane().setSelectedIndex(4);

            }
        });
    }

    private void btnDeleteClickEvent() {
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteLoan();
                resetFields();
            }
        });
    }

    private void btnAddOrUpdateClickEvent() {
        btnAddOrUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loan == null) {
                    addLoan();
                } else {
                    updateLoan();
                }
            }
        });
    }

    // </editor-fold>


    // <editor-fold desc="Persistence">

    private void addLoan() {
        boolean wasInserted = false;
        String resultMessage = "";

        if (!isThisDateValid(txtLoanDate.getText())) {
            resultMessage = "Check loan date. It's a invalid date.";
        } else if (!txtReturnDate.getText().isEmpty()) {
            if (!isThisDateValid(txtReturnDate.getText())) {
                resultMessage = "Check return date. It's a invalid date.";
            }
        } else {

            book = (Book) cmbBoxBookList.getSelectedItem();
            reader = (Reader) cmbBoxReaderList.getSelectedItem();


            if (txtReturnDate.getText().isEmpty()) {
                loan = new Loan(0, book, reader, txtLoanDate.getText(), null);
            } else {
                loan = new Loan(0, book, reader, txtLoanDate.getText(), txtReturnDate.getText());
            }
            wasInserted = loanMgmtViewController.addLoan(loan);

            if (wasInserted) {
                if (loan.getReturnDate() == null) {
                    loanMgmtViewController.updateBookAvailability(loan.getBook(), -1);
                }
                resultMessage = "Successful insert!";

            } else {
                resultMessage = "This loan is already register";
                setModels();
            }
        }
        messageBuilder(wasInserted, resultMessage);
        resetFields();
        MainView.getInstance().getTabPane().setSelectedIndex(4);

    }

    private void updateLoan() {
        boolean wasUpdated = false;
        String resultMessage = "";

        LocalDate loanDate = LocalDate.parse(loan.getLoanDate());
        LocalDate returnDate = LocalDate.parse(txtReturnDate.getText());

        book = (Book) cmbBoxBookList.getSelectedItem();
        reader = (Reader) cmbBoxReaderList.getSelectedItem();

        if (book.getId() != loan.getBook().getId() || reader.getId() != loan.getReader().getId()) {
            resultMessage = "Impossible to change book or reader, please delete than create new loan.";

        } else if (txtReturnDate.getText() == null) {
            resultMessage = "Nothing to update!";

        } else if (!isThisDateValid(txtReturnDate.getText())) {
            resultMessage = "Invalid return date format";
        } else if (loanDate.isAfter(returnDate)) {
            resultMessage = "Impossible to update, return date cannot be before loan date.";
        } else {
            loan.setReturnDate(txtReturnDate.getText());

            wasUpdated = loanMgmtViewController.updateLoan(loan);

            if (wasUpdated) {
                resultMessage = "Successful update loan: \nBook: " + loan.getBook().getTitle() + "\nReader: " + loan.getReader().getFirstName() + " " + loan.getReader().getLastName() +
                        "\nloan date: " + loan.getLoanDate() + "\nReturn date: " + loan.getReturnDate();
                         loanMgmtViewController.updateBookAvailability(loan.getBook(), 1);

            } else {
                setModels();
            }
        }
        messageBuilder(wasUpdated, resultMessage);
        resetFields();
        MainView.getInstance().getTabPane().setSelectedIndex(4);
    }

    private void deleteLoan() {
        boolean wasDeleted = false;
        String resultMessage = "";

        if (loan.getReturnDate() == null) {
            wasDeleted = loanMgmtViewController.deleteLoan(loan);

            if (wasDeleted) {
                loanMgmtViewController.updateBookAvailability(loan.getBook(), 1);
                resultMessage = "Loan Deleted! \nBook: " + loan.getBook().getTitle() + "\nReader: " + loan.getReader().getFirstName() + " " + loan.getReader().getLastName();
            } else {
                resultMessage = "This Loan cannot be deleted!";
                setModels();
            }
        }
        messageBuilder(wasDeleted, resultMessage);
        resetFields();
        MainView.getInstance().getTabPane().setSelectedIndex(4);


    }
    // </editor-fold>


    // <editor-fold desc="Helpers">
    public JPanel getJPanel() {
        return jPanelLoanMgmtView;
    }

    private void setTxtField(JTextField txtField, String txt) {
        txtField.setText(txt);
    }

    private void updateBookList(ArrayList<Book> bookList) {
        cmbBoxBookList.removeAllItems();
        for (Book book : bookList) {
            cmbBoxBookList.addItem(book);

        }
    }

    private void updateReaderList(ArrayList<Reader> readerList) {
        cmbBoxReaderList.removeAllItems();
        for (Reader reader : readerList) {
            cmbBoxReaderList.addItem(reader);
        }
    }

    private void resetFields() {
        setModels();
        updateBookList(LoanMgmtViewController.getInstance().getAllBooksAvailableForLoans());
        updateReaderList(LoanMgmtViewController.getInstance().getAllReaderEnableForLoans());

        setTxtField(txtBookTitle, null);
        setTxtField(txtReader, null);
        setTxtField(txtReturnDate, null);
        setTxtField(txtLoanDate, getDate());

        setLoanDateEditable(true);
        setBtnDeleteVisible(false);

        setBtnTxtAddOrUp("Insert");
        setCmbBoxSelection();



    }

    private void setModels() {
        book = null;
        reader = null;
        loan = null;
    }

    private String getDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        return today.format(formatter);
    }

    private void setBtnDeleteVisible(boolean bol) {
        btnDelete.setVisible(bol);
    }

    private void setLoanDateEditable(boolean bol) {
        txtLoanDate.setEditable(bol);
    }

    private void setBtnTxtAddOrUp(String txt) {
        btnAddOrUpdate.setText(txt);
    }

    private void setCmbBoxSelection() {
        cmbBoxBookList.setSelectedIndex(0);
        cmbBoxReaderList.setSelectedIndex(0);

    }

    public void initLoanUpdate(Loan loan) {
        this.loan = loan;
        cmbBoxSelectingBookAndReader();
        txtLoanDate.setText(loan.getLoanDate());
        setBtnDeleteVisible(true);
        setLoanDateEditable(false);
        setBtnTxtAddOrUp("Update");

    }

    private void cmbBoxSelectingBookAndReader() {

        book = BookViewController.getInstance().getBookById(loan.getBook().getId());
        reader = ReaderViewController.getInstance().getReaderById(loan.getReader().getId());


        for (int i = 0; i < cmbBoxBookList.getItemCount(); i++) {
            if (((Book) cmbBoxBookList.getItemAt(i)).getId() == book.getId()) {
                cmbBoxBookList.setSelectedIndex(i);
                break;
            }

        }
        for (int i = 0; i < cmbBoxReaderList.getItemCount(); i++) {
            if (((Reader) cmbBoxReaderList.getItemAt(i)).getId() == reader.getId()) {
                cmbBoxReaderList.setSelectedIndex(i);
                break;
            }
        }

    }

    private boolean isThisDateValid(String inputDate) {
        if (inputDate == null) {
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            LocalDate dateTime = LocalDate.parse(inputDate, formatter);
            if (dateTime.isAfter(LocalDate.now())) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void messageBuilder(boolean action, String resultMessage) {
        String dialogTitle = (action) ? "SUCCESS" : "ERROR";
        int messageType = (action) ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(null, resultMessage, dialogTitle, messageType);
    }

    public void refreshLoanMgmtViewTab() {
        resetFields();
    }
    // </editor-fold>
}
