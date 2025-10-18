package mentcare.ui;

import com.gustaveandjesse.mentcare.DBHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PatientPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public PatientPanel() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new Object[]{"ID","Name","DOB","Gender","Contact","Last Visit"}, 0);
        table = new JTable(model);
        loadPatients();

        JPanel top = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        JTextField search = new JTextField(20);
        JButton refresh = new JButton("Refresh");
        top.add(addBtn); top.add(editBtn); top.add(delBtn);
        top.add(new JLabel("Search:")); top.add(search); top.add(refresh);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        addBtn.addActionListener(e -> openAddDialog());
        editBtn.addActionListener(e -> openEditDialog());
        delBtn.addActionListener(e -> deleteSelected());
        refresh.addActionListener(e -> loadPatients());
        search.addActionListener(e -> searchPatients(search.getText().trim()));
    }

    private void loadPatients(){
        model.setRowCount(0);
        try (Connection c = DBHelper.getInstance().getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT patient_id,name,dob,gender,contact,last_visit FROM patients")){
            while (rs.next()){
                model.addRow(new Object[]{
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getString("dob"),
                        rs.getString("gender"),
                        rs.getString("contact"),
                        rs.getString("last_visit")
                });
            }
        } catch (SQLException ex){ ex.printStackTrace(); }
    }

    private void searchPatients(String q){
        model.setRowCount(0);
        String sql = "SELECT patient_id,name,dob,gender,contact,last_visit FROM patients WHERE name LIKE ?";
        try (Connection c = DBHelper.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, "%" + q + "%");
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    model.addRow(new Object[]{
                            rs.getInt("patient_id"),
                            rs.getString("name"),
                            rs.getString("dob"),
                            rs.getString("gender"),
                            rs.getString("contact"),
                            rs.getString("last_visit")
                    });
                }
            }
        } catch (SQLException ex){ ex.printStackTrace(); }
    }

    private void openAddDialog(){
        JTextField nameF = new JTextField();
        JTextField dobF = new JTextField();
        JTextField genderF = new JTextField();
        JTextField contactF = new JTextField();
        JTextArea summaryA = new JTextArea(4,20);

        JPanel p = new JPanel(new GridLayout(0,1));
        p.add(new JLabel("Name:")); p.add(nameF);
        p.add(new JLabel("DOB (yyyy-mm-dd):")); p.add(dobF);
        p.add(new JLabel("Gender:")); p.add(genderF);
        p.add(new JLabel("Contact:")); p.add(contactF);
        p.add(new JLabel("Summary:")); p.add(new JScrollPane(summaryA));

        int ok = JOptionPane.showConfirmDialog(this, p, "Add Patient", JOptionPane.OK_CANCEL_OPTION);
        if(ok == JOptionPane.OK_OPTION){
            String sql = "INSERT INTO patients(name,dob,gender,contact,summary,last_visit) VALUES(?,?,?,?,?,?)";
            try (Connection c = DBHelper.getInstance().getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)){
                ps.setString(1, nameF.getText().trim());
                ps.setString(2, dobF.getText().trim());
                ps.setString(3, genderF.getText().trim());
                ps.setString(4, contactF.getText().trim());
                ps.setString(5, summaryA.getText().trim());
                ps.setString(6, dobF.getText().trim()); // initial last_visit maybe same
                ps.executeUpdate();
                loadPatients();
            } catch (SQLException ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Error adding patient."); }
        }
    }

    private void openEditDialog(){
        int row = table.getSelectedRow();
        if(row < 0){ JOptionPane.showMessageDialog(this,"Select a patient first."); return; }
        int id = (int) model.getValueAt(row, 0);
        try (Connection c = DBHelper.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT name,dob,gender,contact,summary,last_visit FROM patients WHERE patient_id=?")){
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    JTextField nameF = new JTextField(rs.getString("name"));
                    JTextField dobF = new JTextField(rs.getString("dob"));
                    JTextField genderF = new JTextField(rs.getString("gender"));
                    JTextField contactF = new JTextField(rs.getString("contact"));
                    JTextArea summaryA = new JTextArea(rs.getString("summary"),4,20);

                    JPanel p = new JPanel(new GridLayout(0,1));
                    p.add(new JLabel("Name:")); p.add(nameF);
                    p.add(new JLabel("DOB (yyyy-mm-dd):")); p.add(dobF);
                    p.add(new JLabel("Gender:")); p.add(genderF);
                    p.add(new JLabel("Contact:")); p.add(contactF);
                    p.add(new JLabel("Summary:")); p.add(new JScrollPane(summaryA));

                    int ok = JOptionPane.showConfirmDialog(this,p,"Edit Patient",JOptionPane.OK_CANCEL_OPTION);
                    if(ok==JOptionPane.OK_OPTION){
                        String sql = "UPDATE patients SET name=?,dob=?,gender=?,contact=?,summary=? WHERE patient_id=?";
                        try (PreparedStatement up = c.prepareStatement(sql)){
                            up.setString(1,nameF.getText().trim());
                            up.setString(2,dobF.getText().trim());
                            up.setString(3,genderF.getText().trim());
                            up.setString(4,contactF.getText().trim());
                            up.setString(5,summaryA.getText().trim());
                            up.setInt(6,id);
                            up.executeUpdate();
                            loadPatients();
                        }
                    }
                }
            }
        } catch (SQLException ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Error editing patient."); }
    }

    private void deleteSelected(){
        int row = table.getSelectedRow();
        if(row < 0){ JOptionPane.showMessageDialog(this,"Select a patient first."); return; }
        int id = (int) model.getValueAt(row, 0);
        int c = JOptionPane.showConfirmDialog(this,"Delete patient ID "+id+"?","Confirm",JOptionPane.YES_NO_OPTION);
        if(c == JOptionPane.YES_OPTION){
            try (Connection conn = DBHelper.getInstance().getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM patients WHERE patient_id=?")){
                ps.setInt(1,id);
                ps.executeUpdate();
                loadPatients();
            } catch (SQLException ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Error deleting patient."); }
        }
    }
}
