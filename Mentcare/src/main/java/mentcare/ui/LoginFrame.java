package mentcare.ui;

import com.gustaveandjesse.mentcare.DBHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    public LoginFrame() {
        setTitle("MentCare - Login");
        setSize(360,220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }
    private void initComponents(){
        JPanel p = new JPanel();
        p.setLayout(null);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(30,30,80,25);
        p.add(lblUser);

        userField = new JTextField();
        userField.setBounds(120,30,180,25);
        p.add(userField);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(30,70,80,25);
        p.add(lblPass);

        passField = new JPasswordField();
        passField.setBounds(120,70,180,25);
        p.add(passField);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(120,110,100,30);
        p.add(btnLogin);

        btnLogin.addActionListener(e -> doLogin());
        getContentPane().add(p);
    }

    private void doLogin(){
        String u = userField.getText().trim();
        String p = new String(passField.getPassword()).trim();
        if(u.isEmpty() || p.isEmpty()){
            JOptionPane.showMessageDialog(this, "Enter username and password.");
            return;
        }
        try (Connection c = DBHelper.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT name,role FROM staff WHERE username=? AND password=?")){
            ps.setString(1,u); ps.setString(2,p);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    String name = rs.getString("name");
                    String role = rs.getString("role");
                    // open dashboard
                    DashboardFrame dash = new DashboardFrame(name, role);
                    dash.setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials.");
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,"Database error: "+ex.getMessage());
        }
    }
}
