package com.vanessaiandrade.library.views;


import com.vanessaiandrade.library.controllers.ReaderViewController;
import com.vanessaiandrade.library.models.Reader;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class ReaderView {

    // <editor-fold desc="Vars">

    private static ReaderView instance;
    private final ReaderViewController readerViewController = ReaderViewController.getInstance();
    private JPanel jPanelReaderView;
    private JPanel jPanelReaderFilter;
    private JTextField txtReaderSearch;
    private JButton btnClear;
    private JTable tableReaders;
    private Reader reader;

    // </editor-fold>


    // <editor-fold desc="Constructor">

    private ReaderView() {
        initView();


    }

    public static ReaderView getInstance() {
        if (instance == null) {
            instance = new ReaderView();
            return instance;
        }

        return instance;
    }

    private void initView() {
        updateReaderTable(readerViewController.getAllReaders());
        txtReaderSearchChangeListener();
        btnClearClickEvent();
        tableReaderMouseListener();

    }
    // </editor-fold>


    // <editor-fold desc="Events">
    private void txtReaderSearchChangeListener() {
        txtReaderSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (reader == null) {
                    if (txtReaderSearch.getText().isEmpty()) {
                        updateReaderTable(readerViewController.getAllReaders());
                    } else {
                        String parameter = txtReaderSearch.getText();
                        Reader readerToSearch = new Reader(0, parameter, parameter, parameter, null);
                        updateReaderTable(readerViewController.searchReader(readerToSearch));
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (reader == null) {
                    if (txtReaderSearch.getText().isEmpty()) {
                        updateReaderTable(readerViewController.getAllReaders());
                    } else {
                        String parameter = txtReaderSearch.getText();
                        Reader readerToSearch = new Reader(0, parameter, parameter, parameter, null);
                        updateReaderTable(readerViewController.searchReader(readerToSearch));
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

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

    private void tableReaderMouseListener() {
        tableReaders.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    getBookWhenRowWasSelected();
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
        return jPanelReaderView;
    }

    private void updateReaderTable(Vector<Reader> readers) {
        tableReaders.setDefaultEditor(Object.class, null);

        tableReaders.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 14));

        DefaultTableModel tableModel = (DefaultTableModel) tableReaders.getModel();

        tableModel.setRowCount(0);

        tableModel.setColumnIdentifiers(getTableColumnIdentifiers());
        for (Reader reader : readers) {
            Vector<Object> tempVector = new Vector<>();
            tempVector.add(reader.getId());
            tempVector.add(reader.getFirstName());
            tempVector.add(reader.getLastName());
            tempVector.add(reader.getDateOfBirth());
            tempVector.add(reader.isLoanEnabled());

            tableModel.addRow(tempVector);
        }
        tableDefinitions();
    }

    private Vector<String> getTableColumnIdentifiers() {
        Vector<String> columnIdentifiers = new Vector<String>();
        columnIdentifiers.add("Id");
        columnIdentifiers.add("First Name");
        columnIdentifiers.add("Last Name");
        columnIdentifiers.add("Birth");
        columnIdentifiers.add("Loan Enabled");


        return columnIdentifiers;
    }

    private void tableDefinitions() {
        tableReaders.setDefaultEditor(Object.class, null);
        tableReaders.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tableReaders.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tableReaders.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableReaders.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tableReaders.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tableHidingIdColumn();
    }

    private void tableHidingIdColumn() {
        TableColumn columnToHide = tableReaders.getColumnModel().getColumn(0);
        columnToHide.setMinWidth(0);
        columnToHide.setMaxWidth(0);
        columnToHide.setWidth(0);
        columnToHide.setPreferredWidth(0);
    }

    private void resetFields(){
        reader = null;
        txtReaderSearch.setText(null);
        tableReaders.clearSelection();
        updateReaderTable(readerViewController.getAllReaders());

    }

    private void getBookWhenRowWasSelected() {
        int id = (int)tableReaders.getValueAt(tableReaders.getSelectedRow(), 0);

        reader = readerViewController.getReaderById(id);

        ReaderManagementView.getInstance().initReaderUpdate(reader);
        MainView.getInstance().getTabPane().setSelectedIndex(3);


    }

    public void refreshReaderViewTab(){
        resetFields();
    }

    // </editor-fold>


}
