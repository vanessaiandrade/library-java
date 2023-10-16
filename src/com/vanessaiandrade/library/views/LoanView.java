package com.vanessaiandrade.library.views;

import com.vanessaiandrade.library.controllers.LoanViewController;
import com.vanessaiandrade.library.models.Book;
import com.vanessaiandrade.library.models.Loan;
import com.vanessaiandrade.library.models.Reader;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class LoanView {

    // <editor-fold desc="Vars">

    private static LoanView instance;
    private final LoanViewController loanViewController = LoanViewController.getInstance();
    private JPanel jPanelLoanView;
    private JPanel jPanelLoanFilter;
    private JLabel labelDateRange;
    private JTextField txtLoanSearch;
    private JTextField txtStartDateRange;
    private JTextField txtEndDateRange;
    private JCheckBox ckBokOpenLoan;
    private JButton btnClear;
    private JTable tableLoans;
    private Loan loan;



    // </editor-fold>


    // <editor-fold desc="Constructor">

    private LoanView() {
        initView();
    }

    public static LoanView getInstance() {
        if (instance == null) {
            instance = new LoanView();
            return instance;
        }

        return instance;
    }

    private void initView() {
        updateLoanTable(loanViewController.getAllLoans());
        txtLoanSearchChangeListener();
        btnClearClickEvent();
        txtStartDateRangeChangeListener();
        txtEndDateRangeChangeListener();
        ckBokOpenLoanChangeListener();
        tableLoansMouseListener();
    }
    // </editor-fold>


    // <editor-fold desc="Events">

    private void txtLoanSearchChangeListener() {
        txtLoanSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                helperTableEvent();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                helperTableEvent();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                helperTableEvent();
            }
        });
    }

    private void btnClearClickEvent() {
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });
    }

    private void txtStartDateRangeChangeListener() {
        txtStartDateRange.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                helperTableEvent();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                helperTableEvent();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                helperTableEvent();
            }
        });
    }

    private void txtEndDateRangeChangeListener() {
        txtEndDateRange.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                helperTableEvent();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                helperTableEvent();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                helperTableEvent();
            }
        });
    }

    private void ckBokOpenLoanChangeListener() {
        ckBokOpenLoan.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                helperTableEvent();
            }
        });
    }

    private void tableLoansMouseListener() {
        tableLoans.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    getLoanWhenRowWasSelected();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }

    // </editor-fold>


    // <editor-fold desc="Helpers">
    public JPanel getJPanel() {
        return jPanelLoanView;
    }

    private void updateLoanTable(Vector<Loan> loans) {

        DefaultTableModel tableModel = (DefaultTableModel) tableLoans.getModel();
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(getTableColumnIdentifiers());

        for (Loan loan : loans) {
            Vector<Object> tempVector = new Vector<>();
            tempVector.add(loan.getId());
            tempVector.add(loan.getBook().getTitle());
            tempVector.add(loan.getReader().getFirstName() + " " + loan.getReader().getLastName());
            tempVector.add(loan.getLoanDate());
            tempVector.add(loan.getReturnDate());

            tableModel.addRow(tempVector);
        }
        tableDefinitions();

    }

    private void tableDefinitions() {
        tableLoans.setDefaultEditor(Object.class, null);
        tableLoans.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tableLoans.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tableLoans.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableLoans.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tableLoans.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tableHidingIdColumn();
    }

    private Vector<String> getTableColumnIdentifiers() {
        Vector<String> columnIdentifiers = new Vector<>();
        columnIdentifiers.add("Loan Id");
        columnIdentifiers.add("Book's Title");
        columnIdentifiers.add("Reader's Name");
        columnIdentifiers.add("Loan Date");
        columnIdentifiers.add("Return Date");

        return columnIdentifiers;

    }

    private void tableHidingIdColumn() {
        TableColumn columnToHide = tableLoans.getColumnModel().getColumn(0);
        columnToHide.setMinWidth(0);
        columnToHide.setMaxWidth(0);
        columnToHide.setWidth(0);
        columnToHide.setPreferredWidth(0);
    }

    private void helperTableEvent() {
        if (loan == null) {
            if (txtLoanSearch.getText().isEmpty() && txtStartDateRange.getText().isEmpty() && txtEndDateRange.getText().isEmpty() && !ckBokOpenLoan.isSelected()) {
                updateLoanTable(loanViewController.getAllLoans());
            } else {
                Loan searchLoan = new Loan(0, null, null, null, null);
                updateLoanTable(loanViewController.searchLoan(searchLoan, txtLoanSearch.getText(), txtStartDateRange.getText(), txtEndDateRange.getText(), ckBokOpenLoan.isSelected()));
            }
        }
    }

    private void resetFields() {
        loan = null;

        setTxtField(txtLoanSearch, null);
        setTxtField(txtStartDateRange, null);
        setTxtField(txtEndDateRange, null);
        ckBokOpenLoan.setSelected(false);

        tableLoans.clearSelection();
        updateLoanTable(loanViewController.getAllLoans());

    }

    private void setTxtField(JTextField txtField, String txt) {
        txtField.setText(txt);
    }

    private void getLoanWhenRowWasSelected() {
        int id = (int) tableLoans.getValueAt(tableLoans.getSelectedRow(), 0);

        loan = loanViewController.getLoanById(id);

        if (loan.getReturnDate() == null) {
            LoanManagementView.getInstance().initLoanUpdate(loan);
            MainView.getInstance().getTabPane().setSelectedIndex(5);
        }
    }

    public void refreshLoanViewTab() {
        resetFields();
    }


// </editor-fold>


}
