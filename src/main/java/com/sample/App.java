package com.sample;

import com.sample.model.Client;
import com.sample.model.Item;
import com.sample.utils.DialogUtils;
import com.sample.view.Main;
import com.sample.view.SetupClientDialog;
import com.sample.view.SetupItemsDialog;

import javax.swing.*;
import java.util.List;

public class App {

    public static final void main(String[] args) {
        setup();
    }

    private static void setup() {
        SetupClientDialog setupClientDialog = new SetupClientDialog(null);
        SetupItemsDialog setupItemsDialog = new SetupItemsDialog(null);

        Client client = setupClientDialog.showDialog();

        if (client != null) {
            List<Item> items = setupItemsDialog.showDialog();

            if (items != null && !items.isEmpty()) {
                new Main(client, items).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Store items is not valid! Exiting...");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Client is not valid! Exiting...");
        }
    }
}
