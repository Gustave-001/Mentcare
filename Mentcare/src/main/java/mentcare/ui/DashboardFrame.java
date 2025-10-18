package mentcare.ui;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private String userName;
    private String role;
    public DashboardFrame(String name, String role) {
        this.userName = name;
        this.role = role;
        setTitle("MentCare - Dashboard (" + userName + " - " + role + ")");
        setSize(1100,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI(){
        JPanel left = new JPanel();
        left.setLayout(new GridLayout(6,1,5,5));
        JButton btnPatients = new JButton("Patient Management");
        JButton btnAppts = new JButton("Appointments");
        JButton btnMon = new JButton("Monitoring");
        JButton btnReports = new JButton("Reports");
        JButton btnChat = new JButton("Chatbot");
        JButton btnLogout = new JButton("Logout");

        left.add(btnPatients); left.add(btnAppts); left.add(btnMon);
        left.add(btnReports); left.add(btnChat); left.add(btnLogout);

        JPanel center = new JPanel(new BorderLayout());
        // start with patient panel
        PatientPanel patientPanel = new PatientPanel();
        AppointmentPanel appointmentPanel = new AppointmentPanel();
        MonitoringPanel monitoringPanel = new MonitoringPanel();
        ReportsPanel reportsPanel = new ReportsPanel();
        ChatbotPanel chatbotPanel = new ChatbotPanel();

        center.add(patientPanel, BorderLayout.CENTER);

        btnPatients.addActionListener(e -> {
            center.removeAll(); center.add(patientPanel); center.revalidate(); center.repaint();
        });
        btnAppts.addActionListener(e -> {
            center.removeAll(); center.add(appointmentPanel); center.revalidate(); center.repaint();
        });
        btnMon.addActionListener(e -> {
            center.removeAll(); center.add(monitoringPanel); center.revalidate(); center.repaint();
        });
        btnReports.addActionListener(e -> {
            center.removeAll(); center.add(reportsPanel); center.revalidate(); center.repaint();
        });
        btnChat.addActionListener(e -> {
            center.removeAll(); center.add(chatbotPanel); center.revalidate(); center.repaint();
        });
        btnLogout.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(left, BorderLayout.WEST);
        getContentPane().add(center, BorderLayout.CENTER);
    }
}
