package com.sample.utils;

import javax.swing.*;
import java.awt.*;

public class DialogUtils {
    public static JDialog getProgressDialog(JFrame parent, String text) {
        JDialog dialog = new JDialog(parent ,text);
        JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        progressBar.setIndeterminate(true);
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
        dialog.add(progressBar);
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(parent);


        return dialog;
    }
}
