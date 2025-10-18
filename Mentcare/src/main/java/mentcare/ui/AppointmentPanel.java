package mentcare.ui;

import com.gustaveandjesse.mentcare.DBHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AppointmentPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;

    public AppointmentPanel() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new Object[]{"ID","Patient ID","Patient Name","Date","Time","Status"},0);
        table = new JTable(model);
        loadAppointments();

        JPanel top = new JPanel();
        JButton add = new JButton("Create");
        JButton complete = new JButton("Mark Done");
        JButton cancel = new JButton("Cancel");
        JButton refresh = new JButton("Refresh");
        top.add(add); top.add(complete); top.add(cancel); top.add(refresh);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        add.addActionListener(e -> createAppointment());
        complete.addActionListener(e -> updateStatus("Done"));
        cancel.addActionListener(e -> updateStatus("Cancelled"));
        refresh.addActionListener(e -> loadAppointments());
    }

    private void loadAppointments(){
        model.setRowCount(0);
        String sql = "SELECT a.appointment_id,a.patient_id,p.name,a.date,a.time,a.status " +
                "FROM appointments a LEFT JOIN patients p ON a.patient_id = p.patient_id";
        try (Connection c = DBHelper.getInstance().getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)){
            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)
                });
            }
        } catch (SQLException ex){ ex.printStackTrace(); }
    }

    private void createAppointment(){
        JTextField pid = new JTextField();
        JTextField date = new JTextField();
        JTextField time = new JTextField();
        JTextArea notes = new JTextArea(3,20);

        JPanel p = new JPanel(new GridLayout(0,1));
        p.add(new JLabel("Patient ID:")); p.add(pid);
        p.add(new JLabel("Date (yyyy-mm-dd):")); p.add(date);
        p.add(new JLabel("Time (hh:mm):")); p.add(time);
        p.add(new JLabel("Notes:")); p.add(new JScrollPane(notes));

        int ok = JOptionPane.showConfirmDialog(this,p,"Create Appointment",JOptionPane.OK_CANCEL_OPTION);
        if(ok == JOptionPane.OK_OPTION){
            String sql = "INSERT INTO appointments(patient_id,date,time,status,notes) VALUES(?,?,?,?,?)";
            try (Connection c = DBHelper.getInstance().getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)){
                ps.setInt(1, Integer.parseInt(pid.getText().trim()));
                ps.setString(2, date.getText().trim());
                ps.setString(3, time.getText().trim());
                ps.setString(4, "Scheduled");
                ps.setString(5, notes.getText().trim());
                ps.executeUpdate();
                loadAppointments();
            } catch (SQLException ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Error creating appointment: "+ex.getMessage()); }
        }
    }

    private void updateStatus(String newStatus){
        int row = table.getSelectedRow();
        if(row < 0){ JOptionPane.showMessageDialog(this,"Select an appointment first."); return; }
        int id = (int) model.getValueAt(row, 0);
        try (Connection c = DBHelper.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE appointments SET status=? WHERE appointment_id=?")){
            ps.setString(1,newStatus);
            ps.setInt(2,id);
            ps.executeUpdate();
            loadAppointments();
        } catch (SQLException ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this,"Error updating appointment."); }
    }
}
