package com.vanessaiandrade.library.views;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Objects;

public class MainView {

    // <editor-fold desc="Vars">
    private static MainView instance;
    private JTabbedPane tabPaneMainView;
    private JPanel jPanelMainView;


    // </editor-fold>

    // <editor-fold desc="Constructor">

    private MainView() {
        jPanelMainView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JFrame jFrameMainView = new JFrame("Library");
        jFrameMainView.add(jPanelMainView);

        initFrame();

        // <WINDOW DEFINITIONS>
        jFrameMainView.setSize(700, 500);
        jFrameMainView.setLocationRelativeTo(null);
        jFrameMainView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrameMainView.setVisible(true);
        // </WINDOW DEFINITIONS>
    }

    public static MainView getInstance() {
        if (instance == null) {
            instance = new MainView();
        }
        return instance;
    }

    private void initFrame() {
        initTabPane();
        jTabPaneListener();

    }
    // </editor-fold>

    // <editor-fold desc="Events">
    private void jTabPaneListener() {


        tabPaneMainView.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = tabPaneMainView.getSelectedIndex();
                setTabPaneEnable(1, index == 0 || index == 1);
                setTabPaneEnable(3, index == 2 || index == 3);
                setTabPaneEnable(5, index == 4 || index == 5);
            }
        });
    }

    // </editor-fold>

    // <editor-fold desc="Helpers">
    private void initTabPane() {
        tabPaneMainView.addTab(null, (new ImageIcon(Objects.requireNonNull(
                this.getClass().getResource("/book-icon.png")))), BookView.getInstance().getJPanel(), "Book View");
        tabPaneMainView.addTab(null, (new ImageIcon(Objects.requireNonNull(
                this.getClass().getResource("/add-book-icon.png")))), BookManagementView.getInstance().getJPanel(), "Book Management");

        tabPaneMainView.addTab(null, (new ImageIcon(Objects.requireNonNull(
                this.getClass().getResource("/readers-icon.png")))), ReaderView.getInstance().getJPanel(), "Reader View");
        tabPaneMainView.addTab(null, null, ReaderManagementView.getInstance().getJPanel(), "Reader Management");

        tabPaneMainView.addTab(null, (new ImageIcon(Objects.requireNonNull(
                this.getClass().getResource("/loan-icon.png")))), LoanView.getInstance().getJPanel(), "Loan View");
        tabPaneMainView.addTab(null, null, LoanManagementView.getInstance().getJPanel(), "Loan Management");

    }

    private void setTabPaneEnable(int index, boolean isEnabled) {
        tabPaneMainView.setEnabledAt(index, isEnabled);
        for (int i = 0; i < tabPaneMainView.getComponents().length; i++) {
            if (tabPaneMainView.isEnabledAt(i)) {
                switch (i) {
                    case 1 -> tabPaneMainView.setIconAt(i, new ImageIcon(Objects.requireNonNull(
                            this.getClass().getResource("/add-book-icon.png"))));
                    case 3 -> tabPaneMainView.setIconAt(i, new ImageIcon(Objects.requireNonNull(
                            this.getClass().getResource("/add-reader-icon.png"))));
                    case 5 -> tabPaneMainView.setIconAt(i, new ImageIcon(Objects.requireNonNull(
                            this.getClass().getResource("/add-loan-icon.png"))));
                }
            } else if (!tabPaneMainView.isEnabledAt(i)) {
                switch (i) {
                    case 1, 3, 5 -> tabPaneMainView.setIconAt(i, null);
                }
            }

        }
    }

    public JTabbedPane getTabPane() {
        return tabPaneMainView;
    }

    public void refreshAllTabs() {
        BookView.getInstance().refreshBookViewTab();
        ReaderView.getInstance().refreshReaderViewTab();
        LoanView.getInstance().refreshLoanViewTab();
        LoanManagementView.getInstance().refreshLoanMgmtViewTab();
    }


// </editor-fold>


}
