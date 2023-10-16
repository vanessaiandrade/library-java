package com.vanessaiandrade.library.views;


import com.vanessaiandrade.library.controllers.BookViewController;
import com.vanessaiandrade.library.models.Book;

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

public class BookView {

    // <editor-fold desc="Vars">
    private static BookView instance;
   private final BookViewController bookViewController = BookViewController.getInstance();
    private JPanel jPanelBookView;
    private JPanel jPanelBookFilter;
    private JTextField txtBooksSearch;
    private JButton btnClear;
    private JTable tableBooks;
    private Book book;

    // </editor-fold>


    // <editor-fold desc="Constructor">
    private BookView() {
        initView();
    }

    public static BookView getInstance() {
        if (instance == null) {
            instance = new BookView();
            return instance;
        }

        return instance;
    }


    private void initView() {
        updateBookTable(bookViewController.getAllBooks());

        txtBooksSearchChangeListener();
        btnClearClickEvent();
        tableBookMouseListener();
    }
    // </editor-fold>


    // <editor-fold desc="Events">

    private void txtBooksSearchChangeListener() {
        txtBooksSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (book == null) {
                    if (txtBooksSearch.getText().isEmpty()) {
                        updateBookTable(bookViewController.getAllBooks());
                    } else {
                        Book bookToSearch = new Book(0, txtBooksSearch.getText(), 0, 0);
                        updateBookTable(bookViewController.searchBook(bookToSearch));
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (book == null) {
                    if (txtBooksSearch.getText().isEmpty()) {
                        updateBookTable(bookViewController.getAllBooks());
                    } else {
                        Book bookToSearch = new Book(0, txtBooksSearch.getText(), 0, 0);
                        updateBookTable(bookViewController.searchBook(bookToSearch));
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

    private void tableBookMouseListener() {
        tableBooks.addMouseListener(new MouseListener() {
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
        return jPanelBookView;
    }

    private void updateBookTable(Vector<Book> books) {
        tableBooks.setDefaultEditor(Object.class, null);

        tableBooks.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 14));

        DefaultTableModel tblModel = (DefaultTableModel) tableBooks.getModel();

        tblModel.setRowCount(0);

        tblModel.setColumnIdentifiers(getTableColumnIdentifiers());

        for (Book book : books) {
            Vector<Object> tempVector = new Vector<>();
            tempVector.add(book.getId());
            tempVector.add(book.getTitle());
            tempVector.add(book.getTotalAmount());
            tempVector.add(book.getAvailableForLoan());

            tblModel.addRow(tempVector);
        }
        tableDefinitions();

    }

    private Vector<String> getTableColumnIdentifiers() {
        Vector<String> columnIdentifiers = new Vector<String>();
        columnIdentifiers.add("Id");
        columnIdentifiers.add("Book Title");
        columnIdentifiers.add("Amount");
        columnIdentifiers.add("Available for Loan");
        return columnIdentifiers;
    }

    private void tableDefinitions() {
        tableBooks.setDefaultEditor(Object.class, null);
        tableBooks.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tableBooks.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tableBooks.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableBooks.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        tableHidingIdColumn();
    }

    private void tableHidingIdColumn() {
        TableColumn columnToHide = tableBooks.getColumnModel().getColumn(0);
        columnToHide.setMinWidth(0);
        columnToHide.setMaxWidth(0);
        columnToHide.setPreferredWidth(0);
        columnToHide.setWidth(0);
    }

    private void resetFields() {
        book = null;
        txtBooksSearch.setText(null);
        tableBooks.clearSelection();
        updateBookTable(bookViewController.getAllBooks());

    }

    private void getBookWhenRowWasSelected() {
        int id = (int) tableBooks.getValueAt(tableBooks.getSelectedRow(), 0);

        book = bookViewController.getBookById(id);

        BookManagementView.getInstance().initBookUpdate(book);
        MainView.getInstance().getTabPane().setSelectedIndex(1);

    }
    public void refreshBookViewTab(){
        resetFields();
    }
    // </editor-fold>
}




