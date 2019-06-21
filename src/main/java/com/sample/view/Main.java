package com.sample.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.sample.Constants.Gender;
import com.sample.Constants.MaritalStatus;
import com.sample.model.Client;
import com.sample.model.Item;
import com.sample.model.ItemsModel;
import com.sample.model.Store;
import com.sample.utils.DialogUtils;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main extends JFrame {
    private Client client;
    private Store store;

    private JPanel mainPanel;
    private JButton buttonBuy;
    private JTextField textFieldQuantity;
    private JTextField textFieldPurchaseValue;
    private JButton buttonFinalizePurchase;
    private JTable tableStore;
    private JTable tableClient;
    private JTextField textFieldFilter;
    private TableRowSorter<ItemsModel> rowSorter;

    private ItemsModel storeModel;
    private ItemsModel clientModel;

    private KieSession kSession;

    public Main(Client client, List<Item> items) {
        JDialog loading = DialogUtils.getProgressDialog(this, "Loading drools...");
        CompletableFuture.runAsync(() -> {
            loading.setVisible(true);
        });

        this.client = client;
        this.store = new Store(items);

        this.initializeUI();

        try {
            KieServices ks = KieServices.Factory.get();
            KieContainer kContainer = ks.getKieClasspathContainer();
            kSession = kContainer.newKieSession("ksession-rules");

            kSession.setGlobal("QUANTITY", this.textFieldQuantity);
            kSession.setGlobal("PURCHASE_VALUE", this.textFieldPurchaseValue);

            kSession.setGlobal("STORE", this.storeModel);
            kSession.setGlobal("CLIENT", this.clientModel);

            kSession.insert(this.client);
            kSession.insert(this.store);

            // enums are not facts, it's needed to insert them into memory, see more:
            // https://stackoverflow.com/questions/54039293/using-enum-in-drools
            for (Gender gender : Gender.values()) {
                kSession.insert(gender);
            }
            for (MaritalStatus status : MaritalStatus.values()) {
                kSession.insert(status);
            }
        } catch (Exception e) {
            System.exit(0);
        }

        loading.setVisible(false);
    }

    private void onFinalizePurchase() {
        JDialog dialog = new ResultsDialog(client.getTotalCartItems(), client.getPurchaseValue());
        dialog.setVisible(true);
    }

    private void initializeUI() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(750, 500));
        pack();

        textFieldQuantity.setText("0");
        textFieldPurchaseValue.setText("0");

        setLocationRelativeTo(null);

        buttonBuy.addActionListener(actionEvent -> onBuy());
        buttonFinalizePurchase.addActionListener(actionEvent -> onFinalizePurchase());

        setupStoreList();
        setupCartList();
    }

    private void setupStoreList() {
        final JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem detailsMenuItem = new JMenuItem("Details");
        JMenuItem addMenuItem = new JMenuItem("Add to cart");

        popupMenu.add(detailsMenuItem);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(addMenuItem);

        detailsMenuItem.addActionListener(actionEvent -> {
            Item selected = (Item) storeModel.getValueAt(tableStore.convertRowIndexToModel(tableStore.getSelectedRow()), -1);
            if (selected != null)
                new ItemDetailsDialog(this, selected, getItemQuantity(selected)).setVisible(true);
        });

        addMenuItem.addActionListener(actionEvent -> {
            onBuy();
        });

        tableStore.setComponentPopupMenu(popupMenu);

        storeModel = new ItemsModel(store.getItems());
        tableStore.setModel(storeModel);
        tableStore.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableStore.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int row = tableStore.rowAtPoint(e.getPoint());
                if (row != -1 && !tableStore.isRowSelected(row)) {
                    tableStore.setRowSelectionInterval(row, row);
                }
            }
        });

        rowSorter = new TableRowSorter(tableStore.getModel());
        tableStore.setRowSorter(rowSorter);

        textFieldFilter.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = textFieldFilter.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = textFieldFilter.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        });
    }


    private void setupCartList() {
        final JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem detailsMenuItem = new JMenuItem("Details");

        popupMenu.add(detailsMenuItem);
        popupMenu.add(new JPopupMenu.Separator());

        detailsMenuItem.addActionListener(actionEvent -> {
            Item selected = (Item) storeModel.getValueAt(tableStore.convertRowIndexToModel(tableStore.getSelectedRow()), -1);
            if (selected != null)
                new ItemDetailsDialog(this, selected, getItemQuantity(selected)).setVisible(true);
        });

        tableClient.setComponentPopupMenu(popupMenu);

        clientModel = new ItemsModel(client.getCart());
        tableClient.setModel(clientModel);
        tableClient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableClient.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int row = tableClient.rowAtPoint(e.getPoint());
                if (row != -1 && !tableClient.isRowSelected(row)) {
                    tableClient.setRowSelectionInterval(row, row);
                }
            }
        });
    }

    private void onBuy() {
        Item selected = (Item) storeModel.getValueAt(tableStore.convertRowIndexToModel(tableStore.getSelectedRow()), -1);
        if (selected != null) {
            kSession.insert(selected);
            kSession.fireAllRules();
        }
    }

    private int getItemQuantity(Item item) {
        long quantity = store.getItems().stream().filter(i -> i.equals(item)).count();
        return (int) quantity;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 2, new Insets(0, 10, 10, 10), -1, -1));
        mainPanel.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Wharehouse quantity");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Purchase value");
        panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldQuantity = new JTextField();
        textFieldQuantity.setBackground(new Color(-1));
        textFieldQuantity.setEditable(false);
        textFieldQuantity.setEnabled(true);
        panel3.add(textFieldQuantity, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textFieldPurchaseValue = new JTextField();
        textFieldPurchaseValue.setBackground(new Color(-1));
        textFieldPurchaseValue.setEditable(false);
        textFieldPurchaseValue.setEnabled(true);
        panel3.add(textFieldPurchaseValue, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonBuy = new JButton();
        buttonBuy.setText("Buy");
        panel5.add(buttonBuy, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel5.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonFinalizePurchase = new JButton();
        buttonFinalizePurchase.setText("Finalize purchase");
        panel5.add(buttonFinalizePurchase, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableStore = new JTable();
        scrollPane1.setViewportView(tableStore);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel1.add(scrollPane2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableClient = new JTable();
        scrollPane2.setViewportView(tableClient);
        textFieldFilter = new JTextField();
        panel1.add(textFieldFilter, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
