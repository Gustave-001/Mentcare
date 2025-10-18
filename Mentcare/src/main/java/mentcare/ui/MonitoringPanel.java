package mentcare.ui;

import com.gustaveandjesse.mentcare.DBHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MonitoringPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;

    public MonitoringPanel() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new Object[]{"Appt ID","Patient ID","Name","Date","Time","Status"},0);
        table = new JTable(model);
        loadMissedAppointments();

        JPanel top = new JPanel();
        JButton refresh = new JButton("Refresh");
        top.add(new JLabel("Potential Issues / Missed Appointments"));
        top.add(refresh);
        refresh.addActionListener(e -> loadMissedAppointments());

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadMissedAppointments(){
        model.setRowCount(0);
        // Simple logic: status = Scheduled & date < today -> missed
        String sql = "SELECT a.appointment_id,a.patient_id,p.name,a.date,a.time,a.status " +
                "FROM appointments a LEFT JOIN patients p ON a.patient_id = p.patient_id " +
                "WHERE a.status = 'Scheduled' AND date(a.date) < date('now')";
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
}
