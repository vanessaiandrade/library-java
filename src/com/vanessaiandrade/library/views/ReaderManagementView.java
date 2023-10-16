package com.vanessaiandrade.library.views;

import com.vanessaiandrade.library.controllers.ReaderMgmtViewController;
import com.vanessaiandrade.library.models.Reader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReaderManagementView {

    // <editor-fold desc="Vars">
    private static ReaderManagementView instance;
    private final ReaderMgmtViewController readerMgmtViewController = ReaderMgmtViewController.getInstance();
    private JPanel jPanelReaderMgmtView;
    private JLabel labelFirstName;
    private JLabel labelDateOfBirth;
    private JLabel labelLastName;
    private JLabel labelDatePattern;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtDateOfBirth;
    private JButton btnCancel;
    private JButton btnAddOrUpdate;
    private JCheckBox ckBoxEnableLoan;
    private Reader reader;

    // </editor-fold>


    // <editor-fold desc="Constructor">

    private ReaderManagementView() {
        initView();


    }

    public static ReaderManagementView getInstance() {
        if (instance == null) {
            instance = new ReaderManagementView();
            return instance;
        }

        return instance;
    }

    private void initView() {
        btnCancelClickEvent();
        btnAddOrUpdateClickEvent();
    }

    // </editor-fold>


    // <editor-fold desc="Events">
    private void btnCancelClickEvent() {
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
                MainView.getInstance().getTabPane().setSelectedIndex(2);

            }
        });
    }

    private void btnAddOrUpdateClickEvent() {
        btnAddOrUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (reader != null) {
                    updateReader();
                } else {
                    try {
                        addReader();
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    // </editor-fold>


    // <editor-fold desc="Persistence">

    private void addReader() throws ParseException {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String dateOfBirth = txtDateOfBirth.getText().trim();
        ckBoxEnableLoan.setSelected(true);

        boolean wasInserted = false;
        String resultMessage = "";

        if (firstName.isEmpty() && lastName.isEmpty() && dateOfBirth.isEmpty()) {
            resultMessage = "First name, last name and date of birth are required.";
        } else if (firstName.isEmpty()) {
            resultMessage = "First name is required.";
        } else if (lastName.isEmpty()) {
            resultMessage = "Last name is required.";
        } else if (dateOfBirth.isEmpty()) {
            resultMessage = "Date of birth is required.";
        } else if (!isThisDateValid(dateOfBirth)) {
            resultMessage = "It's an invalid date";
        } else {
            Reader reader = new Reader(0, firstName, lastName, dateOfBirth, ckBoxEnableLoan.isSelected());

            wasInserted = readerMgmtViewController.addReader(reader);

            if (wasInserted) {
                resultMessage = "Successful insert!\nReader: " + firstName + " " + lastName + "\nBirth: " + dateOfBirth + ".";
            } else {
                resultMessage = "This reader is already available, please select reader to update.";
            }
        }
        messageBuilder((wasInserted), resultMessage);
        resetFields();
        MainView.getInstance().getTabPane().setSelectedIndex(2);
    }

    private void updateReader() {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String dateOfBirth = txtDateOfBirth.getText().trim();


        boolean wasUpdated = false;
        String resultMessage = "";

        if (firstName.isEmpty() || lastName.isEmpty() || dateOfBirth.isEmpty()) {
            resultMessage = "All fields required";
        } else {
            reader.setFirstName(firstName);
            reader.setLastName(lastName);
            reader.setDateOfBirth(dateOfBirth);
            reader.setLoanEnabled(ckBoxEnableLoan.isSelected());

            wasUpdated = readerMgmtViewController.updateReader(reader);

            if (wasUpdated) {
                resultMessage = "Successful update \nReader: " + firstName + " " + lastName + "\nBirth Date: " + dateOfBirth + "\n" + checkBoxLoanEnable();
            } else {
                resultMessage = "Unsuccessful Update";
            }

        }
        messageBuilder((wasUpdated), resultMessage);
        resetFields();
        MainView.getInstance().getTabPane().setSelectedIndex(2);

    }

    // </editor-fold>


    // <editor-fold desc="Helpers">
    public JPanel getJPanel() {
        return jPanelReaderMgmtView;
    }

    private void resetFields() {
        reader = null;

        setTxtField(txtFirstName, null);
        setTxtField(txtLastName, null);
        setTxtField(txtDateOfBirth, null);
        ckBoxEnableLoan.setSelected(true);

        setBtnTxtAddOrUp("Insert");

        MainView.getInstance().refreshAllTabs();

    }

    private void setTxtField(JTextField txtField, String txt) {
        txtField.setText(txt);
    }

    private void setBtnTxtAddOrUp(String txt) {
        btnAddOrUpdate.setText(txt);
    }

    private void messageBuilder(boolean action, String resultMessage) {
        String dialogTitle = (action) ? "INSERTED" : "ERROR";
        int messageType = (action) ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(null, resultMessage, dialogTitle, messageType);
    }

    private String checkBoxLoanEnable() {
        if (ckBoxEnableLoan.isSelected()) {
            return "";
        } else {
            return "Loan Blocked!";
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

    public void initReaderUpdate(Reader reader) {
        this.reader = reader;

        setTxtField(txtFirstName, reader.getFirstName());
        setTxtField(txtLastName, reader.getLastName());
        setTxtField(txtDateOfBirth, reader.getDateOfBirth());
        ckBoxEnableLoan.setSelected(reader.isLoanEnabled());

        setBtnTxtAddOrUp("Update");
    }


    // </editor-fold>


}
