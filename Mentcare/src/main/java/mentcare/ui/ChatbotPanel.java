package mentcare.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatbotPanel extends JPanel {
    private JTextArea chatArea;
    private JTextField input;

    public ChatbotPanel() {
        setLayout(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        input = new JTextField();
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(input, BorderLayout.SOUTH);

        chatArea.setText("MentCare Bot: Hello! I can answer non-medical queries (hours, appointments, contact).\n");

        input.addActionListener(e -> {
            String user = input.getText().trim();
            if(user.isEmpty()) return;
            append("You: " + user);
            String reply = generateReply(user);
            append("Bot: " + reply);
            input.setText("");
        });
    }

    private void append(String s){
        chatArea.append(s + "\n");
    }

    private String generateReply(String user){
        String u = user.toLowerCase();
        if(u.contains("hours") || u.contains("open")) {
            return "The clinic is open Mon-Fri, 08:00 to 16:00.";
        }
        if(u.contains("appointment") || u.contains("book")) {
            return "To book an appointment, please use the Appointments section or contact reception at 012-345-6789.";
        }
        if(u.contains("location") || u.contains("where")) {
            return "Our clinic is at 123 Health Rd, Clinic Town.";
        }
        if(u.contains("cancel")) {
            return "To cancel an appointment, use the Appointments panel or call reception. No medical advice provided here.";
        }
        if(u.contains("thanks") || u.contains("thank")) {
            return "You're welcome. If you have non-clinical concerns, I'm here to help!";
        }
        return "Sorry, I can only help with basic clinic info, appointments, and opening hours.";
    }
}

