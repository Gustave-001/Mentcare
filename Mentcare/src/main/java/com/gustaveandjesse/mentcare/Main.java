
package com.gustaveandjesse.mentcare;

import mentcare.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize DB (creates file & tables)
            DBHelper.getInstance().init();
            // Show login
            new LoginFrame().setVisible(true);
        });
    }
}
