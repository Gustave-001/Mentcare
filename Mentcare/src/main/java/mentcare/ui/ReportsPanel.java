package mentcare.ui;

import com.gustaveandjesse.mentcare.DBHelper;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.sql.*;

public class ReportsPanel extends JPanel {
    public ReportsPanel() {
        setLayout(new BorderLayout());
        JPanel p = new JPanel();
        JButton gen = new JButton("Generate Patients Report (CSV)");
        p.add(gen);
        add(p, BorderLayout.NORTH);
        gen.addActionListener(e -> generateCsv());
    }

    private void generateCsv(){
        String file = "patients_report.csv";
        try (Connection c = DBHelper.getInstance().getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT patient_id,name,dob,gender,contact,last_visit FROM patients");
             FileWriter fw = new FileWriter(file)){
            fw.append("ID,Name,DOB,Gender,Contact,LastVisit\n");
            while(rs.next()){
                fw.append(rs.getInt(1)+","+escape(rs.getString(2))+","+rs.getString(3)+","+rs.getString(4)+","+
                        escape(rs.getString(5))+","+rs.getString(6)+"\n");
            }
            fw.flush();
            JOptionPane.showMessageDialog(this,"Report generated: "+file);
        } catch (Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Error generating report."); }
    }
    private String escape(String s){
        if(s==null) return "";
        return s.replace(",", " ");
    }
}
