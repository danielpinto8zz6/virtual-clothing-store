package com.sample.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.sample.model.Client;
import com.sample.model.Item;
import com.sample.model.Store;
import com.sample.utils.DialogUtils;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main extends JFrame {
    private Client client;
    private Store store;

    private JList listCart;
    private JList listItems;
    private JPanel mainPanel;
    private JButton buttonBuy;
    private JTextField textFieldQuantity;
    private JTextField textFieldPurchaseValue;
    private JButton buttonFinalizePurchase;

    private KieSession kSession;

    public Main(Client client, List<Item> items) {
        JDialog loading = DialogUtils.getProgressDialog(this, "Loading drools...");
        CompletableFuture.runAsync(() -> {
            loading.setVisible(true);
        });

        this.initializeUI();

        this.client = client;
        store = new Store(items);
        listItems.setListData(store.getItems().toArray());

        try {
            KieServices ks = KieServices.Factory.get();
            KieContainer kContainer = ks.getKieClasspathContainer();
            kSession = kContainer.newKieSession("ksession-rules");

            kSession.setGlobal("QUANTITY", this.textFieldQuantity);
            kSession.setGlobal("PURCHASE_VALUE", this.textFieldPurchaseValue);
            kSession.setGlobal("STORE_LIST", this.listItems);
            kSession.setGlobal("CART_LIST", this.listCart);
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
            Item item = getSelectedItem(listItems);
            new ItemDetailsDialog(this, item).setVisible(true);
        });

        addMenuItem.addActionListener(actionEvent -> {
            onBuy();
        });

        listItems.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me)    // if right mouse button clicked
                        && !listItems.isSelectionEmpty()            // and list selection is not empty
                        && listItems.locationToIndex(me.getPoint()) // and clicked point is
                        == listItems.getSelectedIndex()) {       //   inside selected item bounds
                    popupMenu.show(listItems, me.getX(), me.getY());
                }
            }
        });
    }

    private void setupCartList() {
        final JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem detailsMenuItem = new JMenuItem("Details");
        JMenuItem removeMenuItem = new JMenuItem("Remove from cart");

        popupMenu.add(detailsMenuItem);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(removeMenuItem);


        detailsMenuItem.addActionListener(actionEvent -> {
            Item item = getSelectedItem(listCart);
            new ItemDetailsDialog(this, item).setVisible(true);
        });

        removeMenuItem.addActionListener(actionEvent -> {
            onRemove();
        });

        listCart.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me)    // if right mouse button clicked
                        && !listCart.isSelectionEmpty()            // and list selection is not empty
                        && listCart.locationToIndex(me.getPoint()) // and clicked point is
                        == listCart.getSelectedIndex()) {       //   inside selected item bounds
                    popupMenu.show(listCart, me.getX(), me.getY());
                }
            }
        });
    }

    private Item getSelectedItem(JList jList) {
        return (Item) jList.getSelectedValue();
    }

    private void onRemove() {
        Item selected = (Item) listCart.getSelectedValue();
        client.removeFromCart(selected);
        store.addItem(selected);
        listItems.setListData(store.getItems().toArray());
        listCart.setListData(client.getCart().toArray());
    }

    private void onBuy() {
        Item selected = (Item) listItems.getSelectedValue();
//        client.addToCart(selected);
//        store.removeItem(selected);
//        listItems.setListData(store.getItems().toArray());
//        listCart.setListData(client.getCart().toArray());

        kSession.insert(selected);
        kSession.insert(client);
        kSession.insert(store);
        kSession.fireAllRules();
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
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 10, 10, 10), -1, -1));
        mainPanel.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        listCart = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        listCart.setModel(defaultListModel1);
        panel2.add(listCart, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
        panel4.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        listItems = new JList();
        panel4.add(listItems, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonBuy = new JButton();
        buttonBuy.setText("Buy");
        panel5.add(buttonBuy, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel5.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonFinalizePurchase = new JButton();
        buttonFinalizePurchase.setText("Finalize purchase");
        panel5.add(buttonFinalizePurchase, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
